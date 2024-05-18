package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DefectImageIntegrationTest extends BaseIntegrationTest {

    String URL = "/defects";
    DefectType defectType;
    DefectStatus defectStatus;
    CausationCategory causationCategory;
    Process process;
    Lot lot;
    Material material;
    Supplier supplier;
    Defect defect;
    Path path;
    MockMultipartFile file;
    byte[] content;

    @BeforeEach
    void setUp() throws IOException {
        commonSetup();

        path = Paths.get("src/test/resources/testimage.jpg");
        content = Files.readAllBytes(path);
        file = new MockMultipartFile("image", "testimage.jpg",
                "image/jpeg", content);

        defectType = setUpDefectType("testDefectType");
        defectStatus = setUpDefectStatus("testDefectStatus");
        causationCategory = setUpCausationCategory("testCausationCategory");
        process = setUpProcess("testProcess");
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
    }

    @Test
    @Transactional
    void shouldSaveDefectImage() throws Exception {

        mockMvc.perform(multipart(URL + "/" + defect.getId() + "/images")
                        .file(file)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn();

        DefectImage savedDefectImage = defectImageRepository.findAll().get(0);

        try {
            Files.deleteIfExists(Path.of(savedDefectImage.getUuid()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + savedDefectImage.getUuid(), e);
        }

        assertEquals(defect.getImages().get(0).getId(), savedDefectImage.getId());
    }

    @Test
    @Transactional
    void shouldReturnNotFoundWhenDefectIsNotFound() throws Exception{

        mockMvc.perform(multipart(URL + "/2" + "/images")
                        .file(file)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        mockMvc.perform(multipart(URL + "/2" + "/images")
                        .file(file))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Transactional
    void shouldGetDefectImageById() throws Exception{
        DefectImage defectImage = setUpDefectImage("testPath");

        DefectImageDto defectImageDto = defectImageMapper.mapToDto(defectImage);

        mockMvc.perform(get(URL + "/images/" + defectImage.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(defectImageDto)));
    }
}
