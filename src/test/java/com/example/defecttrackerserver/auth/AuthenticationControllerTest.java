package com.example.defecttrackerserver.auth;

import com.example.defecttrackerserver.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest extends BaseControllerTest {

    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private AuthenticationService authenticationService;

    @Override
    protected Object getController() {
        return authenticationController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testAuthenticate() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(authenticationService.authenticate(authRequest)).thenReturn(authResponse);

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    public void testRefreshTokenWithValidToken() throws Exception {
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken("newAccessToken")
                .refreshToken("refreshToken")
                .build();

        when(authenticationService.refreshToken(any())).thenReturn(Optional.of(authResponse));

        mockMvc.perform(post("/auth/refresh-token")
                        .header("Authorization", "Bearer refreshToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    public void testRefreshTokenWithInvalidToken() throws Exception {
        when(authenticationService.refreshToken(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/refresh-token")
                        .header("Authorization", "Bearer refreshToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }
}

