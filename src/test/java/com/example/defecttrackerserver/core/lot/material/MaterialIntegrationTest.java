package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MaterialIntegrationTest extends BaseIntegrationTest {
    String URL = "/materials";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveMaterial() throws Exception {
        UserDto userDto = userMapper.mapToDto(user);

        MaterialDto materialDto = new MaterialDto();
        materialDto.setName("testMaterial");
        materialDto.setResponsibleUsers(Set.of(userDto));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Material savedMaterial = materialRepository.findAll().get(0);

        assertEquals(materialDto.getName(), savedMaterial.getName());
        assertEquals(materialDto.getResponsibleUsers().size(), savedMaterial.getResponsibleUsers().size());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        MaterialDto materialDto = new MaterialDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        MaterialDto materialDto = new MaterialDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetMaterialId() throws Exception{
        Material material = setUpMaterial("testMaterial");

        MaterialDto materialDto = materialMapper.mapToDto(material);

        mockMvc.perform(get(URL + "/" + material.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(materialDto)));
    }

    @Test
    @Transactional
    void shouldGetAllMaterials() throws Exception{
        setUpMaterial("testMaterial1");
        setUpMaterial("testMaterial2");

        MvcResult result = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = mapper.readValue(content, List.class);

        assertEquals(2, list.size());
    }

    @Test
    @Transactional
    void shouldUpdateMaterial() throws Exception{
        Material material = setUpMaterial("testMaterial");

        MaterialDto materialDto = materialMapper.mapToDto(material);
        materialDto.setName("UpdatedTestMaterial");
        materialDto.setResponsibleUsers(Set.of(userMapper.mapToDto(user)));

        mockMvc.perform(put(URL + "/" + material.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Optional<Material> savedMaterial =
                materialRepository.findById(material.getId());

        assertTrue(savedMaterial.isPresent());
        assertEquals(materialDto.getName(), savedMaterial.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteMaterialById() throws Exception{
        Material material = setUpMaterial("testMaterial");

        mockMvc.perform(delete(URL + "/" + material.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
