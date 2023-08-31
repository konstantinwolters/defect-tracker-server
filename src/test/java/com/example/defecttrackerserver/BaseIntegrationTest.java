package com.example.defecttrackerserver;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defect.DefectMapper;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.security.SecurityUser;
import com.example.defecttrackerserver.security.jwt.JwtAuthenticationFilter;
import com.example.defecttrackerserver.security.jwt.JwtService;
import com.example.defecttrackerserver.security.rateLimiting.BucketService;
import com.example.defecttrackerserver.security.rateLimiting.RateLimitingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Import({SecurityConfig.class})
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void start() {postgres.start();}

    @AfterAll
    static void stop() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected DefectMapper defectMapper;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected ActionRepository actionRepository;

    @Autowired
    protected ProcessRepository processRepository;

    @Autowired
    protected DefectTypeRepository defectTypeRepository;

    @Autowired
    protected CausationCategoryRepository causationCategoryRepository;

    @Autowired
    protected DefectStatusRepository defectStatusRepository;

    @Autowired
    protected LocationRepository locationRepository;

    @Autowired
    protected LotRepository lotRepository;

    @Autowired
    protected MaterialRepository materialRepository;

    @Autowired
    protected SupplierRepository supplierRepository;

    @Autowired
    protected DefectRepository defectRepository;

    @BeforeEach
    public void commonSetup() {
        Role role = new Role();
        role.setName("ROLE_QA");

        User user = new User();
        user.setId(1);
        user.setUsername("frank");
        user.addRole(role);

        SecurityUser securityUser = new SecurityUser(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        securityUser,
                        null,
                        securityUser.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        RestAssured.baseURI = "http://localhost:" + port;

        actionRepository.deleteAll();
        defectRepository.deleteAll();
        lotRepository.deleteAll();
        materialRepository.deleteAll();
        supplierRepository.deleteAll();
        processRepository.deleteAll();
        locationRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
        defectTypeRepository.deleteAll();
        defectStatusRepository.deleteAll();
    }
}
