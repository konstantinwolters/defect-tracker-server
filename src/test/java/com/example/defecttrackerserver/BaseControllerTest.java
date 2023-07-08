package com.example.defecttrackerserver;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.security.JwtAuthenticationFilter;
import com.example.defecttrackerserver.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Import({SecurityConfig.class})
@WebMvcTest
public abstract class BaseControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    protected AuthenticationProvider authenticationProvider;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(getController())
                .addFilter(new JwtAuthenticationFilter(getJwtService(), getUserDetailsService()))
                .build();
    }

    protected abstract Object getController();

    protected abstract JwtService getJwtService();

    protected abstract UserDetailsService getUserDetailsService();
}
