package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.BaseIntegrationTest;
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
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.security.SecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ActionIntegrationTest extends BaseIntegrationTest {

    ActionDto actionDto;

    @BeforeEach
    @Transactional
    void setUp() {
        super.commonSetup();

        Role role = setUpRole();
        Location location = setUpLocation();
        User user = setUpUser(role, location);
        Material material = setUpMaterial();
        Supplier supplier = setUpSupplier();
        Lot lot = setUpLot(material, supplier);
        Process process = setUpProcess();
        DefectType defectType = setUpDefectType();
        CausationCategory causationCategory = setUpCausationCategory();
        DefectStatus defectStatus = setUpDefectStatus();
        Defect defect = setUpDefect(lot, defectType, defectStatus, causationCategory, process, location, user);

        setAuthentication(user);

        UserDto userDto = userMapper.mapToDto(user);
        actionDto = new ActionDto();
        actionDto.setDescription("description");
        actionDto.setDueDate(LocalDate.now());
        actionDto.setAssignedUsers(Set.of(userDto));
        actionDto.setCreatedBy(userDto);
        actionDto.setDefect(defect.getId());
    }

    @Test
    @Transactional
    void shouldSaveAction() throws Exception {

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isOk());

        Action action = actionRepository.findAll().get(0);

        assertEquals(actionDto.getDescription(), action.getDescription());
        assertEquals(actionDto.getDueDate(), action.getDueDate());
        assertEquals(actionDto.getAssignedUsers().size(), action.getAssignedUsers().size());
        assertEquals(actionDto.getCreatedBy().getUsername(), action.getCreatedBy().getUsername());
        assertEquals(actionDto.getDefect(), action.getDefect().getId());
    }

    @Test
    @Transactional
    void shouldNotSaveActionAndReturn4xx() throws Exception{
        actionDto.setAssignedUsers(null);
        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldGetActionById() throws Exception{
        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isOk());

        Action action = actionRepository.findAll().get(0);

        assertEquals(actionDto.getDescription(), action.getDescription());
        assertEquals(actionDto.getDueDate(), action.getDueDate());
        assertEquals(actionDto.getAssignedUsers().size(), action.getAssignedUsers().size());
        assertEquals(actionDto.getCreatedBy().getUsername(), action.getCreatedBy().getUsername());
        assertEquals(actionDto.getDefect(), action.getDefect().getId());

    }
}
