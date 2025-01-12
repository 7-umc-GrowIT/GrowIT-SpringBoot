package umc.GrowIT.Server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.TermHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.TermConverter;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.domain.enums.TermType;
import umc.GrowIT.Server.domain.enums.UserStatus;
import umc.GrowIT.Server.dto.UserRequestDTO;
import umc.GrowIT.Server.dto.UserResponseDTO;
import umc.GrowIT.Server.repository.TermRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    public static final int TERM_COUNT = 6;
    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponseDTO.TokenDTO createUser(UserRequestDTO.UserInfoDTO userInfoDTO) {
        String email = userInfoDTO.getEmail();
        Optional<User> user = userRepository.findByEmail(email);

        //이미 가입한 회원일 때(이메일 존재) 예외 처리
        if (user.isPresent()) {
            throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }

        //전체 약관 정보가 주어지지 않았을 때 예외 처리
        if (userInfoDTO.getUserTerms().size() < TERM_COUNT) {
            throw new TermHandler(ErrorStatus.ALL_TERMS_REQUIRED);
        }

        //User 엔티티로 변환
        User newUser = UserConverter.toUser(userInfoDTO);
        newUser.encodePassword(passwordEncoder.encode(newUser.getPassword()));

        /*
          사용자 약관 처리 과정
          1. UserInfoDTO 내부 userTerms(term_id, agreed) Stream 형태로 처리
          2. 약관 존재 여부 및 동의 상태 검증
          3. UserTerm 엔티티로 변환
        */
        List<UserTerm> userTerms = userInfoDTO.getUserTerms().stream()
                .map(tempUserTerm -> {
                    Term term = termRepository.findById(tempUserTerm.getTermId())
                            .orElse(null);
                    //존재하지 않는 약관을 요청하면 예외 처리
                    if (term == null) {
                        throw new TermHandler(ErrorStatus.TERM_NOT_FOUND);
                    //필수 약관에 동의하지 않으면 예외 처리
                    } else if (term.getType() == TermType.MANDATORY && !tempUserTerm.getAgreed()) {
                        throw new TermHandler(ErrorStatus.MANDATORY_TERMS_REQUIRED);
                    }
                    return TermConverter.toUserTerm(tempUserTerm.getAgreed(), term, newUser);
                })
                .collect(Collectors.toList());

        newUser.setUserTerms(userTerms);
        userRepository.save(newUser);

        UserResponseDTO.TokenDTO tokenDTO = jwtTokenProvider.generateToken(getAuthentication(newUser)); //JWT 토큰 생성 메소드 호출
        refreshTokenCommandService.createRefreshToken(tokenDTO.getRefreshToken()); //RefreshToken DB 저장

        return tokenDTO;
    }


    @Override
    public UserResponseDTO.TokenDTO emailLogin(UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail();
        String rawPassword = emailLoginDTO.getPassword();

        //사용자 정보 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //사용자 상태 확인
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE); // 탈퇴한 사용자 처리
        }

        //인증 및 토큰 생성
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, rawPassword);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return jwtTokenProvider.generateToken(authentication); //JWT 토큰 생성
        } catch (BadCredentialsException e) {
            //잘못된 자격 증명: 비밀번호 불일치
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }
    }




    @Override
    public void updatePassword(UserRequestDTO.PasswordDTO passwordDTO) {
        if (passwordDTO.getIsVerified() == null || !passwordDTO.getIsVerified()) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        } else {
            if (passwordDTO.getPassword().equals(passwordDTO.getPasswordCheck())) { //비밀번호와 비밀번호 확인이 일치할 때
                String hashedPassword = passwordEncoder.encode(passwordDTO.getPassword()); //비밀번호 해싱
                String email = passwordDTO.getEmail();
                Optional<User> user = userRepository.findByEmail(email); //이메일을 통해 사용자 정보 찾기
                user.orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
                if (user.get().getStatus() == UserStatus.INACTIVE)
                    throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);
                user.get().encodePassword(hashedPassword); //사용자 비밀번호 변경
            } else {
                throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCH);
            }
        }
    }

    private Authentication getAuthentication(User user) {
        //User 정보를 담은 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), //principal
                null, //credentials
                List.of(new SimpleGrantedAuthority(user.getRole().name())) //authorities
        );
        return authentication;
    }
}
