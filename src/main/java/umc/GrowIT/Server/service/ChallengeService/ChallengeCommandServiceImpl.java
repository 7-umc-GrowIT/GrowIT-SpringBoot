package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ChallengeHandler;
import umc.GrowIT.Server.apiPayload.exception.KeywordHandler;
import umc.GrowIT.Server.apiPayload.exception.OpenAIHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.ChallengeConverter;
import umc.GrowIT.Server.domain.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.*;
import umc.GrowIT.Server.service.ImageService.ImageService;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.OpenAIDTO.ChatGPTRequest;
import umc.GrowIT.Server.web.dto.OpenAIDTO.ChatGPTResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeKeywordRepository challengeKeywordRepository;
    private final KeywordRepository keywordRepository;
    private final ImageService imageService;

    @Value("${openai.model2}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Override
    @Transactional
    public ChallengeResponseDTO.SelectChallengeDTO selectChallenges(Long userId, List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_NOT_FOUND));

        // 전체 선택된 챌린지 개수 초기화
        int dailyChallengeCount = 0;
        int randomChallengeCount = 0;

        // 선택한 UserChallenge 저장
        List<UserChallenge> savedUserChallenges = new ArrayList<>();

        for (ChallengeRequestDTO.SelectChallengeRequestDTO selectRequest : selectRequestList) {
            List<Long> challengeIds = selectRequest.getChallengeIds();
            UserChallengeType dtype = selectRequest.getDtype();

            // 현재 dtype에 따라 저장 가능한 최대 개수 확인
            if (dtype == UserChallengeType.DAILY) {
                dailyChallengeCount += challengeIds.size();
                if (dailyChallengeCount > 2) {
                    throw new ChallengeHandler(ErrorStatus.CHALLENGE_DAILY_MAX);
                }
            } else if (dtype == UserChallengeType.RANDOM) {
                randomChallengeCount += challengeIds.size();
                if (randomChallengeCount > 1) {
                    throw new ChallengeHandler(ErrorStatus.CHALLENGE_RANDOM_MAX);
                }
            }

            // 데일리 챌린지와 랜덤 챌린지 합쳐서 최소 1개 이상 선택
            if (dailyChallengeCount == 0 && randomChallengeCount == 0) {
                throw new ChallengeHandler(ErrorStatus.CHALLENGE_AT_LEAST);
            }

            // 각 챌린지를 UserChallenge로 생성 및 저장
            List<UserChallenge> userChallenges = challengeIds.stream()
                    .map(challengeId -> {
                        Challenge challenge = challengeRepository.findById(challengeId)
                                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

                        // UserChallenge 생성
                        UserChallenge userChallenge = ChallengeConverter.createUserChallenge(user, challenge, dtype);
                        return userChallengeRepository.save(userChallenge);
                    })
                    .toList();

            savedUserChallenges.addAll(userChallenges);
        }


        // DTO 변환 및 반환
        return ChallengeConverter.toSelectChallengeDTO(savedUserChallenges);
    }



    @Override
    @Transactional
    public ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        if (userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_ALREADY_EXISTS);
        }

        // 이미지 업로드
        String imageUrl = null;
        if (proofRequest.getCertificationImage() != null && !proofRequest.getCertificationImage().isEmpty()) {
            imageUrl = imageService.upload(proofRequest.getCertificationImage());
        }

        userChallenge.verifyUserChallenge(proofRequest, imageUrl);
        userChallengeRepository.save(userChallenge);
        return ChallengeConverter.toProofDetailsDTO(userChallenge.getChallenge(), userChallenge, imageUrl);
    }

    @Override
    @Transactional
    public ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest) {
        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_NOT_EXISTS));

        String oldImageUrl = userChallenge.getCertificationImage();
        String newImageUrl = oldImageUrl;

        // 새 이미지 업로드 (새 이미지가 있을 경우에만)
        if (updateRequest.getCertificationImage() != null && !updateRequest.getCertificationImage().isEmpty()) {
            if (oldImageUrl != null) {
                imageService.delete(oldImageUrl); // 기존 이미지 삭제
            }
            newImageUrl = imageService.upload(updateRequest.getCertificationImage());
            userChallenge.setCertificationImage(newImageUrl); // 이미지 URL 업데이트
        }

        // 소감 업데이트 (null이 아닌 경우에만 변경)
        if(updateRequest.getThoughts() != null) {
            userChallenge.setThoughts(updateRequest.getThoughts());
        }

        userChallengeRepository.save(userChallenge);
        return ChallengeConverter.toChallengeModifyProofDTO(userChallenge);
    }

    // 삭제
    @Override
    public ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId) {
        // 1. userId를 조회하고 없으면 오류
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. userChallengeId와 userId를 통해 조회하고 없으면 오류
        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        // 3. 진행 중(false)인 챌린지인지 체크, 완료(true)한 챌린지면 오류
        if(userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_COMPLETE);
        }

        // 4. 삭제
        userChallengeRepository.deleteById(userChallengeId);

        // 5. converter 작업
        return ChallengeConverter.toDeletedUserChallenge(userChallenge);
    }

    // 추천
    @Override
    public ChallengeResponseDTO.RecommendChallengesResponseDTO recommend(ChallengeRequestDTO.RecommendChallengesRequestDTO recommendRequest) {
        // 1. DB에서 감정들 조회
//        List<String> emotions = keywordRepository.findAll()
//                .stream()
//                .map(emotion -> emotion.getName())
//                .toList();
//        log.info(emotions.toString());

        // DB 설정이 제대로 안되었기 때문에 테스트용으로 DB ID 1~5까지의 감정 사용
        // TODO 이후 위의 코드로 수정 필요
        List<String> emotions = new ArrayList<>();
        emotions.add("행복한");
        emotions.add("슬픈");
        emotions.add("외로운");
        emotions.add("불안한");
        emotions.add("기쁜");

        // 2. OpenAI API 호출하여 감정 반환 & 반환받은 감정 체크 (3개인지, DB에 있는 것과 동일한지에 따라 오류)
        List<Keyword> analyzedEmotions = analyzeDiary(recommendRequest.getDiaryContent(), emotions.toString());
        log.info("[최종 분석된 감정] : " + analyzedEmotions.stream().map(Keyword::getName).toList().toString());

        // 3. 감정 키워드 2개 랜덤 선택
        List<Keyword> selectedKeyword = selectRandomKeyword(analyzedEmotions);
        log.info("[선택된 감정] : " + selectedKeyword.stream().map(Keyword::getName).toList().toString());

        // 4. 연관관계 맺고 있는 챌린지 중에서 각 랜덤으로 1개씩 선택
        List<Challenge> dailyChallenges = selectDailyChallenge(selectedKeyword);
        log.info("[데일리 챌린지] : " + dailyChallenges.stream().map(Challenge::getTitle).toList().toString());

        // 5. 3)에서 선택되지 않은 챌린지들 중에서 랜덤으로 1개 선택
        Challenge randomChallenge = challengeRepository.findRandomRemainingChallenge(dailyChallenges);
        log.info("[랜덤 챌린지] : " + randomChallenge.getTitle());

        // 6. converter작업
        return ChallengeConverter.toRecommendedChallengeDTO(analyzedEmotions, dailyChallenges, randomChallenge);
    }


    // 일기 분석 (일기 -> 감정)
    private List<Keyword> analyzeDiary(String diaryContent, String emotions) {

        // 프롬프트
        String prompt = String.format("""
            다음 일기를 분석하여 %s 감정들 중 가장 해당되는 감정을 3개 선택해주세요.
            
            일기 내용: %s
            
            반환 결과는 다른 말 없이 감정들만을 []로 둘러싸서 반환해 주세요.
            ex) [감정1, 감정2, 감정3]
            """, emotions, diaryContent);


        // API 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(model, prompt);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);
        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            throw new OpenAIHandler(ErrorStatus.GPT_RESPONSE_EMPTY);
        }
        String result = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        log.info("[GPT 결과] : " + result);


        // 응답 결과 정리 ([]를 제거하고 각 감정을 분리)
        result = result.replace("[", "").replace("]", "").trim(); // 괄호 제거 후 양 옆 공백 제거
        log.info("[결과정리1] : " + result);

        List<String> emotionsList = Arrays.asList(result.split("\\s*,\\s*")); // , 앞뒤 공백 제거하고 감정들을 나누기
        log.info("[결과정리2] : " + emotionsList.toString());


        // 결과 체크
        // 3개인지 체크
        if (emotionsList.size() != 3) {
            throw new OpenAIHandler(ErrorStatus.EMOTIONS_COUNT_INVALID);
        }

        // 중복 체크
        Set<String> uniqueEmotions = new HashSet<>(emotionsList);
        if (uniqueEmotions.size() != emotionsList.size()) {
            throw new OpenAIHandler(ErrorStatus.EMOTIONS_DUPLICATE);
        }

        // DB에 존재하는 감정인지 체크
        List<Keyword> emotionKeywords = uniqueEmotions.stream()
                .map(emotion -> keywordRepository.findByName(emotion)
                        .orElseThrow(() -> new KeywordHandler(ErrorStatus.KEYWORD_NOT_FOUND)))
                .toList()
                ;

        return emotionKeywords;
    }

    // 감정키워드 랜덤 2개 선택
    private List<Keyword> selectRandomKeyword(List<Keyword> analyzedEmotions) {
        Random random = new Random();

        List<Keyword> editableList = new ArrayList<>(analyzedEmotions);
        Collections.shuffle(editableList, random);

        return editableList.subList(0, 2);
    }


    // 데일리챌린지 랜덤 2개 선택
    private List<Challenge> selectDailyChallenge(List<Keyword> selectedKeyword) {
        Random random = new Random();
        List<Challenge> selectedChallenges = new ArrayList<>();  // 중복 방지를 위해 리스트를 사용

        // 전달받은 selectedKeyword를 통해 연관된 챌린지들 조회
        for (Keyword keyword : selectedKeyword) {
            // 매핑 테이블을 통해 해당 키워드와 연관된 챌린지들을 가져오기
            List<ChallengeKeyword> challengeKeywords = challengeKeywordRepository.findByKeywordWithChallenge(keyword);

            // 연관된 챌린지들
            List<Challenge> relatedChallenges = challengeKeywords.stream()
                    .map(ChallengeKeyword::getChallenge)
                    .filter(challenge -> !selectedChallenges.contains(challenge))  // 이미 선택된 챌린지 제외
                    .collect(Collectors.toList());

            if (relatedChallenges.isEmpty()) {
                throw new ChallengeHandler(ErrorStatus.RELATED_CHALLENGE_NOT_FOUND);
            }

            Collections.shuffle(relatedChallenges, random);
            Challenge selectedChallenge = relatedChallenges.get(0);
            selectedChallenges.add(selectedChallenge);

            log.info(keyword.getName() + "과 연관된 " + selectedChallenge.getTitle() + "을 데일리 챌린지로 선택");
        }

        return selectedChallenges;
    }
}