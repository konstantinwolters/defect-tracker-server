package com.example.defecttrackerserver.core.defect.defectComment;

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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DefectCommentIntegrationTest extends BaseIntegrationTest {
    String URL = "/defects";

    Role roleQA;
    Role roleADMIN;
    User user;
    Location location;
    DefectType defectType;
    DefectStatus defectStatus;
    CausationCategory causationCategory;
    Process process;
    Lot lot;
    Material material;
    Supplier supplier;
    Defect defect;

    @BeforeEach
    void setUp() {
        super.commonSetup();

        roleQA = setUpRole("ROLE_QA");
        roleADMIN = setUpRole("ROLE_ADMIN");
        defectType = setUpDefectType("testDefectType");
        defectStatus = setUpDefectStatus("testDefectStatus");
        causationCategory = setUpCausationCategory("testCausationCategory");
        process = setUpProcess("testProcess");
        location = setUpLocation("testLocation");
        user = setUpUser("frank", "email", roleQA, location);
        material = setUpMaterial("testMaterial");
        supplier = setUpSupplier("testSupplier");
        lot = setUpLot("testLotNumber", material, supplier);

        defect = setUpDefect(
                "testDescription",
                lot,
                defectType,
                defectStatus,
                causationCategory,
                process,
                location,
                user
        );


        setAuthentication(user);
    }

    @Test
    @Transactional
    void shouldSaveDefectComment() throws Exception {

        DefectCommentDto defectCommentDto = new DefectCommentDto();
        defectCommentDto.setContent("testContent");

        mockMvc.perform(post(URL + "/" + defect.getId()  + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectCommentDto)))
                .andExpect(status().isOk());

        DefectComment savedDefectComment = defectCommentRepository.findAll().get(0);

        assertEquals(defectCommentDto.getContent(), savedDefectComment.getContent());
        assertEquals(user.getUsername(), savedDefectComment.getCreatedBy().getUsername());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenContentIsNull() throws Exception{
        DefectCommentDto defectCommentDto = new DefectCommentDto();

        mockMvc.perform(post(URL + "/" + defect.getId()  + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectCommentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        DefectCommentDto defectCommentDto = new DefectCommentDto();
        mockMvc.perform(post(URL + "/" + defect.getId()  + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectCommentDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetDefectCommentById() throws Exception{
        DefectComment defectComment = setUpDefectComment("testContent", user);

        DefectCommentDto defectCommentDto = defectCommentMapper.mapToDto(defectComment);

        mockMvc.perform(get(URL + "/comments/" + defectComment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(defectCommentDto)));
    }

    @Test
    @Transactional
    void shouldUpdateDefectComment() throws Exception{
        user.setRoles(Set.of(roleADMIN));
        setAuthentication(user);

        DefectComment defectComment = setUpDefectComment("testContent", user);

        DefectCommentDto defectCommentDto = defectCommentMapper.mapToDto(defectComment);
        defectCommentDto.setContent("updatedTestContent");

        mockMvc.perform(put(URL + "/comments/" + defectComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defectCommentDto)))
                .andExpect(status().isOk());

        Optional<DefectComment> savedDefectComment =
                defectCommentRepository.findById(defectComment.getId());

        assertTrue(savedDefectComment.isPresent());
        assertEquals(defectCommentDto.getContent(), savedDefectComment.get().getContent());
    }

    @Test
    @Transactional
    void shouldDeleteDefectCommentById() throws Exception{
        user.setRoles(Set.of(roleADMIN));
        setAuthentication(user);

        DefectComment defectComment = setUpDefectComment("testContent", user);
        defect.addDefectComment(defectComment);

        mockMvc.perform(delete(URL + "/" + defect.getId() +  "/comments/" + defectComment.getId()))
                .andExpect(status().isNoContent());
    }
}
