package umc.GrowIT.Server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import umc.GrowIT.Server.domain.enums.UserStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class CustomUserDetails implements UserDetails {

    @Setter
    @Getter
    @Id
    private Long id;
    private String emil;
    private String password;
    private UserStatus status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
