package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ActionIntegrationTest extends BaseIntegrationTest {

    Role roleQA;
    Role roleADMIN;
    Location location;
    User user;
    Material material;
    Supplier supplier;
    Lot lot;
    Process process;
    DefectType defectType;
    CausationCategory causationCategory;
    DefectStatus defectStatus;
    Defect defect;

    @BeforeEach
    void setUp() {
        super.commonSetup();

        roleQA = setUpRole("ROLE_QA");
        roleADMIN = setUpRole("ROLE_ADMIN");
        location = setUpLocation("testLocation");
        user = setUpUser("frank", "email", roleQA, location);
        material = setUpMaterial("testMaterial");
        supplier = setUpSupplier("testSupplier");
        lot = setUpLot("testLotNumber", material, supplier);
        process = setUpProcess("testProcess");
        defectType = setUpDefectType("testDefectType");
        causationCategory = setUpCausationCategory("testCausationCategory");
        defectStatus = setUpDefectStatus("testDefectStatus");
        defect = setUpDefect("testDescription", lot, defectType, defectStatus, causationCategory, process, location, user);

        setAuthentication(user);
    }

    @Test
    @Transactional
    void shouldSaveAction() throws Exception {
        UserDto userDto = userMapper.mapToDto(user);
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
        assertEquals(actionDto.getAssignedUsers().size(), action.getAssignedUsers().size());
        assertEquals(actionDto.getCreatedBy().getUsername(), action.getCreatedBy().getUsername());
        assertEquals(actionDto.getDefect(), action.getDefect().getId());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNoAssignedUsers() throws Exception{
        UserDto userDto = userMapper.mapToDto(user);
        ActionDto actionDto = new ActionDto();
        actionDto.setDescription("description");
        actionDto.setDueDate(LocalDate.now());
        actionDto.setCreatedBy(userDto);
        actionDto.setDefect(defect.getId());
        actionDto.setAssignedUsers(null);

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldGetActionById() throws Exception{
        Action action = setUpAction("testDescription", user, defect);
        ActionDto actionDto = actionMapper.mapToDto(action);

        mockMvc.perform(get("/actions/" + action.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actionDto)));
    }

    @Test
    @Transactional
    void shouldDeleteActionById() throws Exception{
        user.setRoles(Set.of(roleADMIN));
        setAuthentication(user);
        Action action = setUpAction("testDescription", user, defect);

        mockMvc.perform(delete("/actions/" + action.getId()))
                .andExpect(status().isNoContent());
    }
}
