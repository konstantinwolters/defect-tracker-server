package com.example.defecttrackerserver.core.auth;

import jakarta.servlet.http.Cookie;
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
        return ResponseEntity.ok(authenticationService.authenticate(request, csrfToken));
    }

    @PostMapping("/authenticate-cookie")
    public ResponseEntity<?> authenticationCookie(
            @RequestBody AuthenticationRequest request
    ) {
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        Cookie jwtCookie = authenticationService.createAuthenticationCookie(request);
        responseBuilder.header(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return responseBuilder.build();
    }
}
