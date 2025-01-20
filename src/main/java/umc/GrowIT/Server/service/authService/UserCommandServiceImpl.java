package umc.GrowIT.Server.service.authService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
import umc.GrowIT.Server.domain.enums.UserStatus;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;
import umc.GrowIT.Server.repository.TermRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.jwt.JwtTokenUtil;

import java.util.Collections;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponseDTO.TokenDTO createUser(UserRequestDTO.UserInfoDTO userInfoDTO) {
        //인증 받지 않았을 때 예외 처리
        if (userInfoDTO.getIsVerified() == null || !userInfoDTO.getIsVerified())
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);

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

        UserResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken(createUserDetails(newUser)); //JWT 토큰 생성 메소드 호출
        RefreshToken refreshToken = refreshTokenCommandService.createRefreshToken(tokenDTO.getRefreshToken(), newUser); //RefreshToken DB 저장

        newUser.setRefreshToken(refreshToken);
        userRepository.save(newUser);

        return tokenDTO;
    }


    @Override
    public UserResponseDTO.TokenDTO emailLogin(UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail(); //사용자가 입력한 email
        String password = emailLoginDTO.getPassword(); //사용자가 입력한 password

        //인증 수행 및 토큰 생성
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password); //인증되지 않은 상태의 Authentication 객체 생성

            Authentication authentication = authenticationManager.authenticate(authenticationToken); //인증 성공 시 인증된 상태의 Authentication 객체 반환, 인증 실패 시 예외 던짐

            return jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal()); //인증 성공 시 JWT 토큰 생성
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND); //사용자가 입력한 email 또는 password 데이터가 데이터베이스에 없을 때 예외 처리
        } catch (DisabledException e) {
            throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE); //탈퇴한 회원일 때 예외 처리
        }
    }

    @Transactional
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
                Optional<User> user = userRepository.findByEmail(email);
                user.orElseThrow(() ->
                        new UserHandler(ErrorStatus.USER_NOT_FOUND));

                //탈퇴한 회원일 때 예외 처리
                if (user.get().getStatus() == UserStatus.INACTIVE)
                    throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);

                //사용자 비밀번호 변경
                String hashedPassword = passwordEncoder.encode(passwordDTO.getPassword());
                user.get().encodePassword(hashedPassword); //사용자 비밀번호 변경
            }
        }
    }

    protected CustomUserDetails createUserDetails(User user){
        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(user.getRole()))),
                user.getId(),
                user.getStatus()
        );
    }

    @Override
    public UserResponseDTO.DeleteUserResponseDTO delete(Long userId) {
        // 1. userId를 통해 조회하고 없으면 오류
        User deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. soft delete로 진행하기 때문에 status를 inactive로 변경
        if(deleteUser.getStatus()==UserStatus.INACTIVE) {
            throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);
        }
        deleteUser.deleteAccount();
        userRepository.save(deleteUser);

        // 3. converter 작업
        return UserConverter.toDeletedUser(deleteUser);
    }
}