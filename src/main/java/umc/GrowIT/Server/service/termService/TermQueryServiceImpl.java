package umc.GrowIT.Server.service.termService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.TermHandler;
import umc.GrowIT.Server.converter.TermConverter;
import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.domain.enums.TermStatus;
import umc.GrowIT.Server.domain.enums.TermType;
import umc.GrowIT.Server.repository.TermRepository;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;

@Service
@RequiredArgsConstructor
public class TermQueryServiceImpl implements TermQueryService{

    private final TermRepository termRepository;

    /**
     * 현행 약관 목록 조회
     */
    public List<TermResponseDTO.TermDTO> getTerms(){
        List<Term> terms = termRepository.findAllByStatus(TermStatus.ACTIVE); // 현행 약관 조회

        return terms.stream()
                .map(TermConverter::toTermDTO)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 약관 유효성 검사 및 UserTerm 엔티티 생성
     * <p>
     * 1. UserInfoDTO 내부 userTerms(term_id, agreed) Stream 형태로 처리
     * <p>
     * 2. 약관 존재 여부 및 동의 상태 검증
     * <p>
     * 3. UserTerm 엔티티로 변환
     */
    @Override
    public List<UserTerm> checkUserTerms(List<TermRequestDTO.UserTermDTO> requestedUserTerms, User newUser) {
        // 현행 약관 조회
        List<Term> terms = termRepository.findAllByStatus(TermStatus.ACTIVE); // 현행 약관 조회

        // 현행 약관 개수 조회
        int activeTermCount = terms.size();

        // 요청된 약관 개수와 현행 약관 개수가 다르면 예외 처리
        if (requestedUserTerms.size() != activeTermCount) {
            throw new TermHandler(ErrorStatus.INVALID_TERM);
        }

        return requestedUserTerms.stream()
                .map(userTerm -> {
                    Term term = termRepository.findById(userTerm.getTermId())
                            .orElse(null);
                    // 존재하지 않는 약관을 요청하면 예외 처리
                    if (term == null) {
                        throw new TermHandler(ErrorStatus.TERM_NOT_FOUND);
                    }
                    // 필수 약관에 동의하지 않으면 예외 처리
                    if (term.getType() == TermType.MANDATORY && !userTerm.getAgreed()) {
                        throw new TermHandler(ErrorStatus.MANDATORY_TERMS_REQUIRED);
                    }
                    // 약관 동의 상태 검증
                    if (term.getStatus() != TermStatus.ACTIVE) {
                        throw new TermHandler(ErrorStatus.INVALID_TERM);
                    }
                    // UserTerm 엔티티 생성
                    return TermConverter.toUserTerm(userTerm.getAgreed(), term, newUser);
                })
                .collect(Collectors.toList());
    }
}
