package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DefectIntegrationTest extends BaseIntegrationTest {

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
        defectType = setUpDefectType("Undefined");
        causationCategory = setUpCausationCategory("testCausationCategory");
        defectStatus = setUpDefectStatus("New");

        setAuthentication(user);
    }

    @Test
    @Transactional
    void shouldSaveDefect() throws Exception {
        UserDto userDto = userMapper.mapToDto(user);

        DefectDto defectDto = new DefectDto();
        defectDto.setDefectStatus(defectStatus.getName());
        defectDto.setDefectType(defectType.getName());
        defectDto.setProcess(process.getName());
        defectDto.setCausationCategory(causationCategory.getName());
        defectDto.setLot(lot.getLotNumber());
        defectDto.setCreatedBy(userDto);
        defectDto.setDescription("testDescription");

        Path path = Paths.get("src/test/resources/testimage.jpg");
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("images", "testimage.jpg",
                "image/jpeg", content);

        MockMultipartFile jsonFile = new MockMultipartFile("defect", "",
                "application/json", objectMapper.writeValueAsString(defectDto).getBytes());

         mockMvc.perform(multipart("/defects")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isOk())
                .andReturn();

        Defect defect = defectRepository.findAll().get(0);

        assertEquals(defectDto.getDescription(), defect.getDescription());
        assertEquals(defectDto.getCreatedBy().getUsername(), defect.getCreatedBy().getUsername());
        assertEquals(defectDto.getDefectStatus(), defect.getDefectStatus().getName());
        assertEquals(defectDto.getDefectType(), defect.getDefectType().getName());
        assertEquals(defectDto.getProcess(), defect.getProcess().getName());
        assertEquals(defectDto.getLot(), defect.getLot().getLotNumber());
    }

//    @Test
//    @Transactional
//    void shouldReturnBadRequestWhenNoAssignedUsers() throws Exception{
//        UserDto userDto = userMapper.mapToDto(user);
//        ActionDto actionDto = new ActionDto();
//        actionDto.setDescription("description");
//        actionDto.setDueDate(LocalDate.now());
//        actionDto.setCreatedBy(userDto);
//        actionDto.setDefect(defect.getId());
//        actionDto.setAssignedUsers(null);
//
//        mockMvc.perform(post("/actions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(actionDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @Transactional
//    void shouldReturn403WhenNotAuthenticated() throws Exception{
//        SecurityContextHolder.clearContext();
//
//        UserDto userDto = userMapper.mapToDto(user);
//        ActionDto actionDto = new ActionDto();
//        actionDto.setDescription("description");
//        actionDto.setDueDate(LocalDate.now());
//        actionDto.setCreatedBy(userDto);
//        actionDto.setDefect(defect.getId());
//        actionDto.setAssignedUsers(Set.of(userDto));
//
//        mockMvc.perform(post("/actions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(actionDto)))
//                .andExpect(status().isForbidden());
//    }
//
    @Test
    @Transactional
    void shouldGetDefectById() throws Exception{
        Defect defect = setUpDefect("testDescription",lot, defectType, defectStatus,
                causationCategory, process, location, user);
        DefectDto defectDto = defectMapper.mapToDto(defect);

        mockMvc.perform(get("/defects/" + defect.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(defectDto)));
    }

    @Test
    @Transactional
    void shouldGetAllDefects() throws Exception{
        setUpDefect("testDescription", lot, defectType, defectStatus, causationCategory,
                process, location, user);

        mockMvc.perform(get("/defects"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0));
    }

    @Test
    @Transactional
    void shouldGetDefectsFilteredByCreatedByUser() throws Exception{
        User user2 = setUpUser("Wolfgang", "mail2", roleQA, location);

        setUpDefect("testDescription", lot, defectType, defectStatus, causationCategory, process, location, user);
        setUpDefect("testDescription2", lot, defectType, defectStatus, causationCategory, process, location, user2);

        String jsonPathExpression = String.format("$.content[0].createdBy.id", user2.getId());

        mockMvc.perform(get("/defects")
                        .param("createdByIds", String.valueOf(user2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first element of content has an assignedUsers array that contains an object with the given id
                .andExpect(jsonPath(jsonPathExpression).exists());
    }

//    @Test
//    @Transactional
//    void shouldGetActionsFilteredBySearchTerm() throws Exception{
//        User user2 = setUpUser("Wolfgang", "mail2", roleQA, location);
//
//        setUpAction("testDescription", user, defect);
//        Action action2 = setUpAction("testDescription2", user2, defect);
//
//        mockMvc.perform(get("/actions")
//                        .param("search", action2.getDescription()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalPages").value(1))
//                .andExpect(jsonPath("$.totalElements").value(1))
//                .andExpect(jsonPath("$.currentPage").value(0))
//                // Check if the first elements description content contains the searchterm
//                .andExpect(jsonPath("$.content[0].description").value(action2.getDescription()));
//    }
//
//    @Test
//    @Transactional
//    void shouldUpdateAction() throws Exception{
//        user.setRoles(Set.of(roleADMIN));
//        setAuthentication(user);
//        Action action = setUpAction("testDescription", user, defect);
//
//        ActionDto actionDto = actionMapper.mapToDto(action);
//        actionDto.setDescription("updatedDescription");
//
//        mockMvc.perform(put("/actions/" + action.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(actionDto)))
//                .andExpect(status().isOk());
//
//        Optional<Action> updatedAction = actionRepository.findById(action.getId());
//
//        assertTrue(updatedAction.isPresent());
//        assertEquals(actionDto.getDescription(), updatedAction.get().getDescription());
//    }
//
//    @Test
//    @Transactional
//    void shouldDeleteActionById() throws Exception{
//        user.setRoles(Set.of(roleADMIN));
//        setAuthentication(user);
//        Action action = setUpAction("testDescription", user, defect);
//
//        mockMvc.perform(delete("/actions/" + action.getId()))
//                .andExpect(status().isNoContent());
//    }
}
