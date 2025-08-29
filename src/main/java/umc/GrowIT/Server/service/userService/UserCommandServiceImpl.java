package umc.GrowIT.Server.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.converter.WithdrawalConverter;
import umc.GrowIT.Server.domain.*;
import umc.GrowIT.Server.domain.CustomUserDetails;
import umc.GrowIT.Server.repository.*;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.service.termService.TermCommandService;
import umc.GrowIT.Server.service.termService.TermQueryService;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.util.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus._BAD_REQUEST;
import static umc.GrowIT.Server.domain.enums.UserStatus.INACTIVE;


@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final WithdrawalReasonRepository withdrawalReasonRepository;
    private final WithdrawalLogRepository withdrawalLogRepository;

    private final TermQueryService termQueryService;
    private final TermCommandService termCommandService;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserWithdrawalService userWithdrawalService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDTO.TokenDTO signupEmail(UserRequestDTO.UserInfoDTO userInfoDTO) {
        //인증 받지 않았을 때 예외 처리
        if (userInfoDTO.getIsVerified() == null || !userInfoDTO.getIsVerified())
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);

        String email = userInfoDTO.getEmail();

        // 이미 이메일 가입 했거나, 카카오 간편 가입한 회원
        if (userRepository.existsByPrimaryEmail(email)) {
            String password = userRepository.findPasswordByPrimaryEmail(email);

            // 이미 이메일 가입한 회원
            if (password != null) {
                throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
            }

            // 카카오 간편 가입한 회원
            User user = userRepository.findByPrimaryEmail(email)
                    .orElseThrow(() -> new UserHandler(_BAD_REQUEST));
            user.linkUserWithKakaoAccount(email, passwordEncoder.encode(userInfoDTO.getPassword())); // 이메일, 비밀번호 업데이트
            termCommandService.updateUserTerms(userInfoDTO.getUserTerms()); // 약관 목록 업데이트
            return issueTokenAndSetRefreshToken(user);
        }

        // 최초 이메일 회원가입
        User newUser = UserConverter.toUser(userInfoDTO);
        newUser.encodePassword(passwordEncoder.encode(newUser.getPassword()));

        // 약관 정보 유효성 검사 및 UserTerm 엔티티 생성
        List<UserTerm> userTerms = termQueryService.checkUserTerms(userInfoDTO.getUserTerms(), newUser);
        newUser.setUserTerms(userTerms);

        // User 엔티티 저장 및 AT/RT 발급
        return issueTokenAndSetRefreshToken(userRepository.save(newUser));
    }

    @Override
    public TokenResponseDTO.TokenDTO loginEmail(UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail(); //사용자가 입력한 email
        String password = emailLoginDTO.getPassword(); //사용자가 입력한 password

        try {
            User user = userRepository.findByPrimaryEmail(email)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            // 탈퇴한 사용자인지 확인
            checkUserInactive(user);

            // 인증 수행 및 토큰 생성 및 저장
            TokenResponseDTO.TokenDTO tokenDTO = performAuthentication(email, password);
            setRefreshToken(tokenDTO.getRefreshToken(), user);

            return tokenDTO;
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND); //사용자가 입력한 email 또는 password 데이터가 데이터베이스에 없을 때 예외 처리
        }
    }

    @Override
    public void updatePassword(UserRequestDTO.PasswordDTO passwordDTO) {
        //인증 받지 않았을 때 예외 처리
        if (passwordDTO.getIsVerified() == null || !passwordDTO.getIsVerified())
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
            //인증 확인 받았을 때 비밀번호 일치 확인 후 비밀번호 변경
        else {
            if (!passwordDTO.getPassword().equals(passwordDTO.getPasswordCheck()))
                throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCH);
            else {
                //사용자 정보 없을 때 예외 처리
                String email = passwordDTO.getEmail();
                Optional<User> user = userRepository.findByPrimaryEmail(email);
                user.orElseThrow(() ->
                        new UserHandler(ErrorStatus.USER_NOT_FOUND));

                //탈퇴한 회원일 때 예외 처리
                if (user.get().getStatus() == INACTIVE)
                    throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);

                //사용자 비밀번호 변경
                String hashedPassword = passwordEncoder.encode(passwordDTO.getPassword());
                user.get().encodePassword(hashedPassword); //사용자 비밀번호 변경
            }
        }
    }


    @Override
    @Transactional
    public void withdraw(Long userId, UserRequestDTO.DeleteUserRequestDTO deleteUserRequestDTO) {
        // 1. TODO 이후 Resolver로 수정
        User deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 탈퇴이유 조회
        Long reasonId = deleteUserRequestDTO.getReasonId();
        WithdrawalReason withdrawalReason = withdrawalReasonRepository.findById(reasonId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.WITHDRAWAL_REASON_NOT_FOUND));

        // 3. 탈퇴기록 저장
        WithdrawalLog withdrawalLog = WithdrawalConverter.toWithdrawalLog(deleteUser, withdrawalReason);
        withdrawalLogRepository.save(withdrawalLog);

        // 4. 해당 사용자 관련 데이터 전체 삭제
        userWithdrawalService.deleteUserRelatedData(deleteUser.getId(), deleteUser.getRefreshToken().getId());
    }



    /**
     * AT/RT 발급 및 RT DB 저장
     */
    @Override
    public TokenResponseDTO.TokenDTO issueTokenAndSetRefreshToken(User user) {
        TokenResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken(
                customUserDetailsService.loadUserByUsername(user.getPrimaryEmail()));

        RefreshToken refreshTokenEntity = refreshTokenCommandService.createRefreshToken(tokenDTO.getRefreshToken(), user);
        user.setRefreshToken(refreshTokenEntity);

        return tokenDTO;
    }

    @Override
    public void setRefreshToken(String refreshToken, User user) {
        RefreshToken refreshTokenEntity = refreshTokenCommandService.createRefreshToken(refreshToken, user);
        user.setRefreshToken(refreshTokenEntity);
    }

    @Override
    public TokenResponseDTO.TokenDTO performAuthentication(String email, String password) {
        //인증되지 않은 상태의 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        //인증 성공 시 인증된 상태의 Authentication 객체 반환, 인증 실패 시 예외 던짐
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //인증 성공 시 JWT 토큰 생성
        TokenResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

        return tokenDTO;
    }

    @Override
    public void checkUserInactive(User user){
        if (user.getStatus() == INACTIVE)
            throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);
    }
}

