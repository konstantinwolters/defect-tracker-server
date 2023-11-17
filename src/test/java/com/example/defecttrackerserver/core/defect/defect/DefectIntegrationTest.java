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
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class DefectIntegrationTest extends BaseIntegrationTest {
    String URL = "/defects";

    @Value("${IMAGE.UPLOAD-PATH}")
    String imageFolderPath;

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
        causationCategory = setUpCausationCategory("Undefined");
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
        defectDto.setLot(lot.getId());
        defectDto.setCreatedBy(userDto);
        defectDto.setDescription("testDescription");

        Path path = Paths.get("src/test/resources/testimage.jpg");
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("images", "testimage.jpg",
                "image/jpeg", content);

        MockMultipartFile jsonFile = new MockMultipartFile("defect", "",
                "application/json", objectMapper.writeValueAsString(defectDto).getBytes());

         mockMvc.perform(multipart(URL)
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isOk())
                .andReturn();

        Defect defect = defectRepository.findAll().get(0);

        defect.getImages().forEach(image -> {
            try {
                Files.deleteIfExists(Path.of(image.getPath()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + image.getPath(), e);
            }
        });

        assertEquals(defectDto.getDescription(), defect.getDescription());
        assertEquals(defectDto.getCreatedBy().getUsername(), defect.getCreatedBy().getUsername());
        assertEquals(defectDto.getDefectStatus(), defect.getDefectStatus().getName());
        assertEquals(defectDto.getDefectType(), defect.getDefectType().getName());
        assertEquals(defectDto.getProcess(), defect.getProcess().getName());
        assertEquals(defectDto.getLot(), defect.getLot().getId());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenLotIsNull() throws Exception{
        UserDto userDto = userMapper.mapToDto(user);

        DefectDto defectDto = new DefectDto();
        defectDto.setDefectStatus(defectStatus.getName());
        defectDto.setDefectType(defectType.getName());
        defectDto.setProcess(process.getName());
        defectDto.setCausationCategory(causationCategory.getName());
        defectDto.setLot(null);
        defectDto.setCreatedBy(userDto);
        defectDto.setDescription("testDescription");

        Path path = Paths.get("src/test/resources/testimage.jpg");
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("images", "testimage.jpg",
                "image/jpeg", content);

        MockMultipartFile jsonFile = new MockMultipartFile("defect", "",
                "application/json", objectMapper.writeValueAsString(defectDto).getBytes());

        mockMvc.perform(multipart(URL)
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        UserDto userDto = userMapper.mapToDto(user);
        DefectDto defectDto = new DefectDto();
        defectDto.setDefectStatus(defectStatus.getName());
        defectDto.setDefectType(defectType.getName());
        defectDto.setProcess(process.getName());
        defectDto.setCausationCategory(causationCategory.getName());
        defectDto.setLot(lot.getId());
        defectDto.setCreatedBy(userDto);
        defectDto.setDescription("testDescription");

        Path path = Paths.get("src/test/resources/testimage.jpg");
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("images", "testimage.jpg",
                "image/jpeg", content);

        MockMultipartFile jsonFile = new MockMultipartFile("defect", "",
                "application/json", objectMapper.writeValueAsString(defectDto).getBytes());

        mockMvc.perform(multipart(URL)
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetDefectById() throws Exception{
        Defect defect = setUpDefect("testDescription",lot, defectType, defectStatus,
                causationCategory, process, location, user);
        DefectDto defectDto = defectMapper.mapToDto(defect);

        mockMvc.perform(get(URL + "/" + defect.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(defectDto)));
    }

    @Test
    @Transactional
    void shouldGetAllDefects() throws Exception{
        setUpDefect("testDescription", lot, defectType, defectStatus, causationCategory,
                process, location, user);

        mockMvc.perform(get(URL))
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

        mockMvc.perform(get(URL)
                        .param("createdByIds", String.valueOf(user2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first element of content has an assignedUsers array that contains an object with the given id
                .andExpect(jsonPath(jsonPathExpression).exists());
    }

    @Test
    @Transactional
    void shouldGetDefectsFilteredBySearchTerm() throws Exception{
        User user2 = setUpUser("Wolfgang", "mail2", roleQA, location);

        setUpDefect("testDescription", lot, defectType, defectStatus, causationCategory, process, location, user);
        Defect defect = setUpDefect("testDescription2", lot, defectType, defectStatus, causationCategory, process, location, user2);

        mockMvc.perform(get(URL)
                        .param("search", defect.getDescription()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                // Check if the first elements description content contains the searchterm
                .andExpect(jsonPath("$.content[0].description").value(defect.getDescription()));
    }

    @Test
    @Transactional
    void shouldUpdateDefect() throws Exception{
        user.setRoles(Set.of(roleADMIN));
        setAuthentication(user);
        Defect defect = setUpDefect("testDescription", lot, defectType,
                defectStatus, causationCategory, process, location, user);

        DefectDto defectDto = defectMapper.mapToDto(defect);
        defectDto.setDescription("updatedDescription");

        Path path = Paths.get("src/test/resources/testimage.jpg");
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("images", "testimage.jpg",
                "image/jpeg", content);

        MockMultipartFile jsonFile = new MockMultipartFile("defect", "",
                "application/json", objectMapper.writeValueAsString(defectDto).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL + "/" + defect.getId())
                        .file(file)
                        .file(jsonFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Defect> updatedDefect = defectRepository.findById(defect.getId());

        defect.getImages().forEach(image -> {
            try {
                Files.deleteIfExists(Path.of(image.getPath()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + image.getPath(), e);
            }
        });

        assertTrue(updatedDefect.isPresent());
        assertEquals(defectDto.getDescription(), updatedDefect.get().getDescription());
    }

    @Test
    @Transactional
    void shouldDeleteDefectById() throws Exception{
        user.setRoles(Set.of(roleADMIN));
        setAuthentication(user);
        Defect defect = setUpDefect("testDescription", lot, defectType, defectStatus, causationCategory, process, location, user);


        mockMvc.perform(delete(URL + "/" + defect.getId()))
                .andExpect(status().isNoContent());
    }
}
