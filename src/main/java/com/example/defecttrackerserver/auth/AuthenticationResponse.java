package com.example.defecttrackerserver.auth;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer object for returning jwt token and refresh token.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private UserDto user;
}
