package com.example.defecttrackerserver.core.lot.supplier;

import com.example.defecttrackerserver.BaseIntegrationTest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SupplierIntegrationTest extends BaseIntegrationTest {
    String URL = "/suppliers";

    @BeforeEach
    void setUp() {
        commonSetup();
    }

    @Test
    @Transactional
    void shouldSaveSupplier() throws Exception {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setName("testSupplier");
        supplierDto.setCustomId("testCustomId");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Supplier savedSupplier = supplierRepository.findAll().get(0);

        assertEquals(supplierDto.getName(), savedSupplier.getName());
        assertEquals(supplierDto.getCustomId(), savedSupplier.getCustomId());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        SupplierDto supplierDto = new SupplierDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        SupplierDto supplierDto = new SupplierDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetSupplierId() throws Exception{
        Supplier supplier = setUpSupplier("testSupplier");

        SupplierDto supplierDto = supplierMapper.mapToDto(supplier);

        mockMvc.perform(get(URL + "/" + supplier.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(supplierDto)));
    }

    @Test
    @Transactional
    void shouldGetAllSuppliers() throws Exception{
        setUpSupplier("testSupplier1");
        setUpSupplier("testSupplier2");

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
    void shouldUpdateSupplier() throws Exception{
        Supplier supplier = setUpSupplier("testSupplier");

        SupplierDto supplierDto = supplierMapper.mapToDto(supplier);
        supplierDto.setName("UpdatedTestSupplier");

        mockMvc.perform(put(URL + "/" + supplier.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Optional<Supplier> savedSupplier =
                supplierRepository.findById(supplier.getId());

        assertTrue(savedSupplier.isPresent());
        assertEquals(supplierDto.getName(), savedSupplier.get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteSupplierById() throws Exception{
        Supplier supplier = setUpSupplier("testSupplier");

        mockMvc.perform(delete(URL + "/" + supplier.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
