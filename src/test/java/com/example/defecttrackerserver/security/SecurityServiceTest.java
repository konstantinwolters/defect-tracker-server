package com.example.defecttrackerserver.security;

import com.example.defecttrackerserver.core.user.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void shouldReturnUsernameFromSecurityContext() {

        UserDetails userDetails = User.builder()
                .username("username")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles("USER")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertEquals("username", securityService.getUsername());
    }

    @Test
    void shouldCheckIfUserHasRole() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "username",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertTrue(securityService.hasRole("ROLE_USER"));
        assertFalse(securityService.hasRole("ROLE_ADMIN"));
    }
}