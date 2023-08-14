package com.example.defecttrackerserver.core.action;

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
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Ignore
public class ActionIntegrationTest {

    @LocalServerPort
    private Integer port;

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
    MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DefectMapper defectMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private DefectTypeRepository defectTypeRepository;

    @Autowired
    private DefectStatusRepository defectStatusRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private DefectRepository defectRepository;

    @BeforeEach
    void setUp() {
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

    @Test
    @Transactional
    @Ignore
    @WithMockUser(username = "frank", roles = "USER")
    void shouldSaveAction() throws Exception {

        Role role = new Role();
        role.setName("ROLE_USER");

        Location location = new Location();
        location.setName("location");
        locationRepository.save(location);

        User user = new User();
        user.setUsername("frank");
        user.setMail("email");
        user.setPassword("password");
        user.setLocation(location);
        user.setIsActive(true);
        user.setRoles(Set.of(role));

        UserDto userDto = userMapper.mapToDto(userRepository.save(user));

        Material material = new Material();
        material.setName("material");
        materialRepository.save(material);

        Supplier supplier = new Supplier();
        supplier.setName("supplier");
        supplierRepository.save(supplier);

        Lot lot = new Lot();
        lot.setLotNumber("lotNumber");
        lot.setMaterial(material);
        lot.setSupplier(supplier);
        lotRepository.save(lot);

        Process process = new Process();
        process.setName("process");
        processRepository.save(process);

        DefectType defectType = new DefectType();
        defectType.setName("defectType");
        defectTypeRepository.save(defectType);

        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName("defectStatus");
        defectStatusRepository.save(defectStatus);

        Defect defect = new Defect();
        defect.setLot(lot);
        defect.setDefectType(defectType);
        defect.setDefectStatus(defectStatus);
        defect.setProcess(process);
        defect.setLocation(location);
        defect.setCreatedBy(user);
        defect.setCreatedAt(LocalDateTime.now());

        defect = defectRepository.save(defect);

        ActionDto actionDto = new ActionDto();
        actionDto.setDescription("description");
        actionDto.setDueDate(LocalDate.now());
        actionDto.setAssignedUsers(Set.of(userDto));
        actionDto.setCreatedBy(userDto);
        actionDto.setDefect(defect.getId());

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isOk());

        Action action = actionRepository.findAll().get(0);

        assertEquals(actionDto.getDescription(), action.getDescription());
        assertEquals(actionDto.getDueDate(), action.getDueDate());
    }
}
