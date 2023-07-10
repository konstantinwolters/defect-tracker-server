package com.example.defecttrackerserver.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            CsrfToken csrfToken
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/authenticate-cookie")
    public ResponseEntity<?> authenticationCookie(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        Cookie jwtCookie = authenticationService.createAuthenticationCookie(request);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok().build();
    }
}
