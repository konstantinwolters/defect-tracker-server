package com.example.defecttrackerserver.auth;

import com.example.defecttrackerserver.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User userDetails;
    private String authHeader;
    private String username;
    @BeforeEach
    void setUp() {
        userDetails = new User("username", "password", new ArrayList<>());
        authHeader = "Bearer refreshToken";
        username = "username";
    }

    @Test
    void shouldAuthenticateRequest() {
        AuthenticationRequest request = new AuthenticationRequest("username", "password");
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";

        when(userDetailsService.loadUserByUsername(request.getUsername())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(userDetails)).thenReturn(refreshToken);

        AuthenticationResponse response = authenticationService.authenticate(request);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void shouldReturnRefreshTokenWithValidHeader() throws IOException {
        String accessToken = "newAccessToken";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authHeader);
        when(jwtService.getUsernameFromToken("refreshToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid("refreshToken", userDetails)).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn(accessToken);

        Optional<AuthenticationResponse> result = authenticationService.refreshToken(request);

        assertTrue(result.isPresent());
        assertEquals(accessToken, result.get().getAccessToken());
        assertEquals("refreshToken", result.get().getRefreshToken());
    }

    @Test
    void shouldNotReturnRefreshTokenWithInvalidHeader() throws IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        Optional<AuthenticationResponse> result = authenticationService.refreshToken(request);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldNotReturnRefreshTokenWithInvalidToken() throws IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authHeader);
        when(jwtService.getUsernameFromToken("refreshToken")).thenReturn(null);

        Optional<AuthenticationResponse> result = authenticationService.refreshToken(request);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldNotReturnRefreshTokenWithInvalidUser() throws IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authHeader);
        when(jwtService.getUsernameFromToken("refreshToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid("refreshToken", userDetails)).thenReturn(false);

        Optional<AuthenticationResponse> result = authenticationService.refreshToken(request);

        assertFalse(result.isPresent());
    }
}
