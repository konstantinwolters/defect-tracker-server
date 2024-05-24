package com.example.defecttrackerserver.auth;

import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.user.user.UserMapper;
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
 * Service for managing authentication.
 * Provides authentication functionality and generates authentication response with JWT token and refresh token.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final EntityService entityService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        var user = userMapper.mapToDto(entityService.getUserByUsername(userDetails.getUsername()));
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    public Optional<AuthenticationResponse> refreshToken(HttpServletRequest request) {
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


