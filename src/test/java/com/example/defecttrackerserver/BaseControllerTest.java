package com.example.defecttrackerserver;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.security.jwt.JwtAuthenticationFilter;
import com.example.defecttrackerserver.security.jwt.JwtService;
import com.example.defecttrackerserver.security.rateLimiting.BucketService;
import com.example.defecttrackerserver.security.rateLimiting.RateLimitingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Import({SecurityConfig.class})
@WebMvcTest
public abstract class BaseControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    protected RateLimitingFilter rateLimitingFilter;

    @MockBean
    protected BucketService bucketService;

    @MockBean
    protected AuthenticationProvider authenticationProvider;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(getController())
                .build();

        when(jwtService.getUsernameFromToken(any())).thenReturn("bill");
        when(bucketService.resolveBucket(anyString())).thenReturn(mock(Bucket.class));
    }

    protected abstract Object getController();
}
