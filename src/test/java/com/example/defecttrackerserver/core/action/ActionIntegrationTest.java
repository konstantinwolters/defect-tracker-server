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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext
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
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        UserDto userDto = userMapper.mapToDto(user);
        ActionDto actionDto = new ActionDto();
        actionDto.setDescription("description");
        actionDto.setDueDate(LocalDate.now());
        actionDto.setCreatedBy(userDto);
        actionDto.setDefect(defect.getId());
        actionDto.setAssignedUsers(Set.of(userDto));

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isForbidden());
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
    void shouldGetAllActions() throws Exception{
        setUpAction("testDescription", user, defect);

        mockMvc.perform(get("/actions"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0));
    }

    @Test
    @Transactional
    void shouldGetActionsFilteredByAssignedUsers() throws Exception{
        User user2 = setUpUser("Wolfgang", "mail2", roleQA, location);

        setUpAction("testDescription", user, defect);
        setUpAction("testDescription2", user2, defect);

        String jsonPathExpression = String.format("$.content[0].assignedUsers[?(@.id==%d)].id", user2.getId());

        mockMvc.perform(get("/actions")
                        .param("assignedUserIds", String.valueOf(user2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first element of content has an assignedUsers array that contains an object with the given id
                .andExpect(jsonPath(jsonPathExpression).exists());
    }

    @Test
    @Transactional
    void shouldGetActionsFilteredBySearchTerm() throws Exception{
        User user2 = setUpUser("Wolfgang", "mail2", roleQA, location);

        setUpAction("testDescription", user, defect);
        Action action2 = setUpAction("testDescription2", user2, defect);

        mockMvc.perform(get("/actions")
                        .param("search", action2.getDescription()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first elements description content contains the searchterm
                .andExpect(jsonPath("$.content[0].description").value(action2.getDescription()));
    }

    @Test
    @Transactional
    void shouldUpdateAction() throws Exception{
        user.setRoles(Set.of(roleADMIN));
        setAuthentication(user);
        Action action = setUpAction("testDescription", user, defect);

        ActionDto actionDto = actionMapper.mapToDto(action);
        actionDto.setDescription("updatedDescription");

        mockMvc.perform(put("/actions/" + action.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionDto)))
                .andExpect(status().isOk());

        Optional<Action> updatedAction = actionRepository.findById(action.getId());

        assertTrue(updatedAction.isPresent());
        assertEquals(actionDto.getDescription(), updatedAction.get().getDescription());
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
