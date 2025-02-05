package umc.GrowIT.Server.service.oAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.exception.OAuthHandler;
import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.repository.OAuthAccountRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.service.termService.TermQueryService;
import umc.GrowIT.Server.service.userService.CustomUserDetailsService;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.util.JwtTokenUtil;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

import java.util.List;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.*;
import static umc.GrowIT.Server.converter.OAuthAccountConverter.toOAuthAccount;
import static umc.GrowIT.Server.converter.UserConverter.toUser;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuthCommandServiceImpl implements OAuthCommandService {

    private final TermQueryService termQueryService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserCommandService userCommandService;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenResponseDTO.TokenDTO signupSocial(OAuthRequestDTO.OAuthUserInfoAndUserTermsDTO oAuthUserInfoAndUserTermsDTO){
        OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfo = oAuthUserInfoAndUserTermsDTO.getOauthUserInfo();
        List<TermRequestDTO.UserTermDTO> requestedUserTerms = oAuthUserInfoAndUserTermsDTO.getUserTerms();

        // 이미 최초 소셜 로그인을 한 경우 가입 불가
        if (oAuthAccountRepository.existsByProviderId(oAuthUserInfo.getId()))
            throw new OAuthHandler(ACCOUNT_ALREADY_EXISTS);

        // 동일한 이메일이 이미 존재하는 경우 가입 불가, 소셜 로그인에서 처리되었어야 함
        if (userRepository.existsByPrimaryEmail(oAuthUserInfo.getEmail()))
            throw new OAuthHandler(ACCOUNT_BAD_REQUEST);

        // User 엔티티 생성
        User newUser = toUser(oAuthUserInfo);

        // UserTerm 유효성 검사 및 엔티티 생성
        List<UserTerm> userTerms = termQueryService.checkUserTerms(requestedUserTerms, newUser);

        // OAuthAccount 엔티티 생성
        OAuthAccount oAuthAccount = toOAuthAccount(oAuthUserInfo, newUser);

        // User, OAuthAccount 엔티티 저장
        newUser.setUserTerms(userTerms);
        userRepository.save(newUser);
        oAuthAccountRepository.save(oAuthAccount);

        // AT/RT 토큰 발급 및 RT DB 저장
        return userCommandService.issueTokenAndSetRefreshToken(newUser);
    }
}
