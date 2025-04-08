package umc.GrowIT.Server.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.CustomUserDetails;
import umc.GrowIT.Server.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String primaryEmail) throws UsernameNotFoundException { //AuthenticationManager 의 인증을 위임받은 DaoAuthenticationProvider 가 호출

        User user = userRepository.findByPrimaryEmail(primaryEmail)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다 : " + primaryEmail)); //사용자 정보 없을 시 예외 던짐

        return new CustomUserDetails(
                user.getPrimaryEmail(),
                user.getPassword() == null ? "null" : user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(user.getRole()))),
                user.getId()
        );
    }
}
