package com.example.defecttrackerserver;

import com.example.defecttrackerserver.config.SecurityConfig;
import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryMapper;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectMapper;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusMapper;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeMapper;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessMapper;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationMapper;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotMapper;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Import({SecurityConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
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
    protected DefectStatusMapper defectStatusMapper;

    @Autowired
    protected DefectTypeMapper defectTypeMapper;

    @Autowired
    protected ProcessMapper processMapper;

    @Autowired
    protected LocationMapper locationMapper;

    @Autowired
    protected CausationCategoryMapper causationCategoryMapper;

    @Autowired
    protected LotMapper lotMapper;

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

    protected Role setUpRole(String name){
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    protected Location setUpLocation(String name){
        Location location = new Location();
        location.setName(name);
        return locationRepository.save(location);
    }

    protected User setUpUser(String username, String mail, Role role, Location location){
        User user = new User();
        user.setUsername(username);
        user.setMail(mail);
        user.setPassword("password");
        user.setLocation(location);
        user.setIsActive(true);
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    protected Material setUpMaterial(String name){
        Material material = new Material();
        material.setName(name);
        return materialRepository.save(material);
    }

    protected Supplier setUpSupplier(String name){
        Supplier supplier = new Supplier();
        supplier.setName(name);
        return supplierRepository.save(supplier);
    }

    protected Lot setUpLot(String lotNumber, Material material, Supplier supplier){
        Lot lot = new Lot();
        lot.setLotNumber(lotNumber);
        lot.setMaterial(material);
        lot.setSupplier(supplier);
        return lotRepository.save(lot);
    }

    protected Process setUpProcess(String name){
        Process process = new Process();
        process.setName(name);
        return processRepository.save(process);
    }

    protected DefectType setUpDefectType(String name) {
        DefectType defectType = new DefectType();
        defectType.setName(name);
        return defectTypeRepository.save(defectType);
    }

    protected CausationCategory setUpCausationCategory(String name) {
        CausationCategory causationCategory = new CausationCategory();
        causationCategory.setName(name);
        return causationCategoryRepository.save(causationCategory);
    }

    protected DefectStatus setUpDefectStatus(String name) {
        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName(name);
        return defectStatusRepository.save(defectStatus);
    }

    protected Defect setUpDefect(String description,
                                 Lot lot,
                                 DefectType defectType,
                                 DefectStatus defectStatus,
                                 CausationCategory causationCategory,
                                 Process process,
                                 Location location,
                                 User user){
        Defect defect = new Defect();
        defect.setDescription(description);
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

    protected Action setUpAction(String description, User user, Defect defect){
        Action action = new Action();
        action.setDescription(description);
        action.setDueDate(LocalDate.now());
        action.setIsCompleted(true);
        user.addAssignedAction(action);
        action.setDefect(defect);
        action.setCreatedAt(LocalDateTime.now());
        action.setChangedAt(LocalDateTime.now());
        action.setCreatedBy(user);
        action.setChangedBy(user);
        return actionRepository.save(action);
    }
}
