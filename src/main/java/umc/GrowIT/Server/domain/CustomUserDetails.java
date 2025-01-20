package umc.GrowIT.Server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import umc.GrowIT.Server.domain.enums.UserStatus;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User{
    private final Long id;
    private final UserStatus status;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, UserStatus status) {
        super(username, password, authorities);
        this.id = id;
        this.status = status;
    }
}
