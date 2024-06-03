package com.example.defecttrackerserver.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer object for username and password for authentication.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class AuthenticationRequest {
    String username;
    String password;
}
