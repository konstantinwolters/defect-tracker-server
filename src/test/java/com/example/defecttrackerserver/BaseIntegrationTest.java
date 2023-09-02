package com.example.defecttrackerserver;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectMapper;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
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
import jakarta.transaction.Transactional;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    protected ActionMapper actionMapper;

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

    protected void setAuthentication(User user){
        SecurityUser securityUser = new SecurityUser(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        securityUser,
                        null,
                        securityUser.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected Role setUpRole(){
        Role role = new Role();
        role.setName("ROLE_QA");
        return roleRepository.save(role);
    }

    protected Location setUpLocation(){
        Location location = new Location();
        location.setName("TestLocation");
        return locationRepository.save(location);
    }

    protected User setUpUser(Role role, Location location){
        User user = new User();
        user.setUsername("frank");
        user.setMail("email");
        user.setPassword("password");
        user.setLocation(location);
        user.setIsActive(true);
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    protected Material setUpMaterial(){
        Material material = new Material();
        material.setName("material");
        return materialRepository.save(material);
    }

    protected Supplier setUpSupplier(){
        Supplier supplier = new Supplier();
        supplier.setName("supplier");
        return supplierRepository.save(supplier);
    }

    protected Lot setUpLot(Material material, Supplier supplier){
        Lot lot = new Lot();
        lot.setLotNumber("lotNumber");
        lot.setMaterial(material);
        lot.setSupplier(supplier);
        return lotRepository.save(lot);
    }

    protected Process setUpProcess(){
        Process process = new Process();
        process.setName("process");
        return processRepository.save(process);
    }

    protected DefectType setUpDefectType() {
        DefectType defectType = new DefectType();
        defectType.setName("defectType");
        return defectTypeRepository.save(defectType);
    }

    protected CausationCategory setUpCausationCategory() {
        CausationCategory causationCategory = new CausationCategory();
        causationCategory.setName("testCategory");
        return causationCategoryRepository.save(causationCategory);
    }

    protected DefectStatus setUpDefectStatus() {
        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName("defectStatus");
        return defectStatusRepository.save(defectStatus);
    }

    protected Defect setUpDefect(Lot lot,
                                 DefectType defectType,
                                 DefectStatus defectStatus,
                                 CausationCategory causationCategory,
                                 Process process,
                                 Location location,
                                 User user){
        Defect defect = new Defect();
        defect.setDescription("test");
        defect.setLot(lot);
        defect.setDefectType(defectType);
        defect.setCausationCategory(causationCategory);
        defect.setDefectStatus(defectStatus);
        defect.setProcess(process);
        defect.setLocation(location);
        defect.setCreatedBy(user);
        defect.setCreatedAt(LocalDateTime.now());
        return defectRepository.save(defect);
    }


}
