package umc.GrowIT.Server.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import umc.GrowIT.Server.domain.*;
import umc.GrowIT.Server.domain.enums.TermType;
import umc.GrowIT.Server.domain.CustomUserDetails;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.service.termService.TermQueryService;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;
import umc.GrowIT.Server.repository.TermRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.util.JwtTokenUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static umc.GrowIT.Server.domain.enums.UserStatus.INACTIVE;


@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final TermQueryService termQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDTO.TokenDTO signupEmail(UserRequestDTO.UserInfoDTO userInfoDTO) {
        //인증 받지 않았을 때 예외 처리
        if (userInfoDTO.getIsVerified() == null || !userInfoDTO.getIsVerified())
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);

        String email = userInfoDTO.getEmail();

        //이미 가입한 회원일 때(이메일 존재) 예외 처리
        if (userRepository.existsByPrimaryEmail(email))
            throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);

        //User 엔티티로 변환
        User newUser = UserConverter.toUser(userInfoDTO);
        newUser.encodePassword(passwordEncoder.encode(newUser.getPassword()));

        //약관 정보 유효성 검사 및 UserTerm 엔티티 생성
        List<UserTerm> userTerms = termQueryService.checkUserTerms(userInfoDTO.getUserTerms(), newUser);

        newUser.setUserTerms(userTerms);
        userRepository.save(newUser);

        //자동 로그인 처리
        TokenResponseDTO.TokenDTO tokenDTO = performAuthentication(userInfoDTO.getEmail(), userInfoDTO.getPassword());

        //refresh token 엔티티 변환 및 user 엔티티에 refresh token 저장
        setRefreshToken(tokenDTO.getRefreshToken(), newUser);

        return tokenDTO;
    }

    @Override
    public TokenResponseDTO.TokenDTO loginEmail(UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail(); //사용자가 입력한 email
        String password = emailLoginDTO.getPassword(); //사용자가 입력한 password

        //인증 수행 및 토큰 생성
        try {
            User user = userRepository.findByPrimaryEmail(email)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            if (user.getStatus() == INACTIVE)
                throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);

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
    public CustomUserDetails createUserDetails(User user) {
        return new CustomUserDetails(
                user.getPrimaryEmail(),
                Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(user.getRole()))),
                user.getId()
        );
    }

    @Override
    public UserResponseDTO.DeleteUserResponseDTO delete(Long userId) {
        // 1. userId를 통해 조회하고 없으면 오류
        User deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. soft delete로 진행하기 때문에 status를 inactive로 변경
        if (deleteUser.getStatus() == INACTIVE) {
            throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);
        }
        deleteUser.deleteAccount();
        userRepository.save(deleteUser);

        // 3. converter 작업
        return UserConverter.toDeletedUser(deleteUser);
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
}

