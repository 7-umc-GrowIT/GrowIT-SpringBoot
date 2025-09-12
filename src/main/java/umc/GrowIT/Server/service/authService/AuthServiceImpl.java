package umc.GrowIT.Server.service.authService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.AuthHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.AuthenticationCodeConverter;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.domain.AuthenticationCode;
import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.repository.AuthenticationCodeRepository;
import umc.GrowIT.Server.repository.OAuthAccountRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenCommandService refreshTokenCommandService;
    private final EmailService emailService;

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final AuthenticationCodeRepository authenticationCodeRepository;

    @Override
    @Transactional
    public void sendAuthEmail(AuthType type, AuthRequestDTO.SendAuthEmailRequestDTO request) {
        // 1. type 구분해서 email 체크
        checkEmail(type, request.getEmail());

        // 2. 랜덤 번호 생성
        String authenticationCode = createAuthenticationCode();

        // 3. 이메일 전송
        emailService.sendEmailAsync(request.getEmail(), authenticationCode);

        // 4. 인증코드 유효성 체크 & 저장
        saveAuthenticationCode(request.getEmail(), authenticationCode);
    }


    @Override
    @Transactional
    public void verifyAuthCode(AuthRequestDTO.VerifyAuthCodeRequestDTO request) {
        // 1. 이메일 & 유효기간으로 인증번호 조회
        AuthenticationCode authenticationCode = authenticationCodeRepository
                .findByEmailAndIsVerifiedFalseAndExpirationDateAfter(request.getEmail(), LocalDateTime.now())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.AUTH_CODE_NOT_FOUND));

        // 2. request와 인증번호 비교
        if(!authenticationCode.getCode().equals(request.getAuthCode())) {
            throw new AuthHandler(ErrorStatus.AUTH_CODE_MISMATCH);
        }

        // 3. 만료처리
        authenticationCode.verify();
    }


    @Override
    @Transactional
    public AuthResponseDTO.LogoutResponseDTO logout(Long userId){

        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. RefreshToken이 있는 경우에만 삭제 처리
        if (user.getRefreshToken() != null) {
            // RefreshToken DB에서 삭제
            RefreshToken refreshToken = user.getRefreshToken();
            user.deleteRefreshToken(); // User 엔티티의 refreshToken 필드를 null로 설정
            userRepository.save(user);

            // RefreshToken 엔티티 삭제
            refreshTokenCommandService.deleteRefreshToken(refreshToken);
        }

        // 3. 성공 응답 반환 (이미 로그아웃된 상태여도 성공으로 처리)
        return UserConverter.toLogoutDTO();
    }


    /*
    이 아래부터 헬퍼메소드
    */

    // 이메일 체크
    public void checkEmail(AuthType type, String email) {
        User user = userRepository.findByPrimaryEmail(email)
                .orElse(null);

        boolean oAuthExists = oAuthAccountRepository.existsByEmail(email);

        switch (type) {
            case SIGNUP -> {
                if (user != null) {
                    // User & OAuth에 해당 이메일 존재
                    if (oAuthExists) {
                        // 일반 & 소셜 회원가입한 이메일
                        if (user.getPassword() != null) {
                            throw new AuthHandler(ErrorStatus.EMAIL_EXISTS_GENERAL);
                        }
                        // 소셜 회원가입만 한 이메일
                        else {
                            throw new AuthHandler(ErrorStatus.EMAIL_EXISTS_SOCIAL);
                        }
                    }

                    // User에만 해당 이메일 존재
                    else {
                        // 일반 회원가입만 한 이메일
                        throw new AuthHandler(ErrorStatus.EMAIL_EXISTS_GENERAL);
                    }
                }
            }
            case PASSWORD_RESET -> {
                // 회원가입하지 않은 이메일이기 때문에 비밀번호 변경 불가
                if (user == null) {
                    throw new AuthHandler(ErrorStatus.EMAIL_NOT_FOUND);
                }
                // 소셜 회원가입만 한 이메일이기 때문에 비밀번호 변경 불가
                if (user.getPassword() == null) {
                    throw new AuthHandler(ErrorStatus.PASSWORD_RESET_UNAVAILABLE);
                }
            }
            default -> {
                throw new AuthHandler(ErrorStatus.INVALID_AUTH_TYPE);
            }
        }
    }

    // 8자리 랜덤 문자or숫자 문자열 생성
    private String createAuthenticationCode() {
        return RandomStringUtils.random(8, true, true);
    }

    // 유효성 체크 & 저장
    private void saveAuthenticationCode(String email, String authenticationCode) {
        // 1. 이전 유효한 인증 코드가 있는지 확인 & 있다면 무효화
        authenticationCodeRepository.findByEmailAndIsVerifiedFalseAndExpirationDateAfter(email, LocalDateTime.now())
                .ifPresent(AuthenticationCode::expire);

        // 2. 새 인증 코드 저장
        AuthenticationCode newAuthenticationCode = AuthenticationCodeConverter.toAuthenticationCode(email, authenticationCode);
        authenticationCodeRepository.save(newAuthenticationCode);
    }
}
