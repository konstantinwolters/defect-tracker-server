package com.example.defecttrackerserver.security;

import com.example.defecttrackerserver.core.user.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of Spring Security {@link UserDetails}
 */
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private final User user;

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public Integer getId() { return user.getId();}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(SecurityRole::new)
                .collect(Collectors.toList());
    }

       @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }

    @Override
    public String toString() {
        return "SecurityUser{" +
                "id=" + user.getId() +
                ", username='" + user.getUsername() + '\'' +
                '}';
    }
}
