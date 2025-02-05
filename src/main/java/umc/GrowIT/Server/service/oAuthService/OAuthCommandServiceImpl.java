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
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.util.JwtTokenUtil;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

import java.util.List;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.ACCOUNT_ALREADY_EXISTS;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.EMAIL_ALREADY_EXISTS;
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

    public TokenResponseDTO.TokenDTO signupSocial(OAuthRequestDTO.OAuthUserInfoAndUserTermsDTO oAuthUserInfoAndUserTermsDTO){
        OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfo = oAuthUserInfoAndUserTermsDTO.getOauthUserInfo();
        List<TermRequestDTO.UserTermDTO> requestedUserTerms = oAuthUserInfoAndUserTermsDTO.getUserTerms();

        if (oAuthAccountRepository.existsByProviderId(oAuthUserInfo.getId()))
            throw new OAuthHandler(ACCOUNT_ALREADY_EXISTS);

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

        // AT/RT 토큰 발급 및 RT는 DB에 저장
        TokenResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken(
                userCommandService.createUserDetails(newUser));
        userCommandService.setRefreshToken(tokenDTO.getRefreshToken(), newUser);

        return tokenDTO;
    }
}
