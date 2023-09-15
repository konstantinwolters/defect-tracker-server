package com.example.defecttrackerserver.auth;

import com.example.defecttrackerserver.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * Provides authentication functionality and generates authentication response with JWT token and refresh token.
 *
 * @author Konstantin Wolters
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userDetailsService.loadUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Optional<AuthenticationResponse> refreshToken(HttpServletRequest request) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.getUsernameFromToken(refreshToken);
        if (username == null) {
            return Optional.empty();
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if(!jwtService.isTokenValid(refreshToken, userDetails)){
            return Optional.empty();
        }

        var accessToken = jwtService.generateToken(userDetails);
        return Optional.of(AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }
}


