package com.example.defecttrackerserver.security;

import com.example.defecttrackerserver.core.user.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    @MockBean
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