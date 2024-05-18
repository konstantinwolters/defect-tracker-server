package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.BaseIntegrationTest;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
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

public class LotIntegrationTest extends BaseIntegrationTest {
    String URL = "/lots";
    Material material;
    Supplier supplier;

    @BeforeEach
    void setUp() {
        commonSetup();

        material = setUpMaterial("testMaterial");
        supplier = setUpSupplier("testSupplier");
    }

    @Test
    @Transactional
    void shouldSaveLot() throws Exception {
        MaterialDto materialDto = materialMapper.mapToDto(material);
        SupplierDto supplierDto = supplierMapper.mapToDto(supplier);

        LotDto lotDto = new LotDto();
        lotDto.setLotNumber("testLotNumber");
        lotDto.setMaterial(materialDto);
        lotDto.setSupplier(supplierDto);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lotDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Lot savedLot = lotRepository.findAll().get(0);

        assertEquals(lotDto.getLotNumber(), savedLot.getLotNumber());
        assertEquals(lotDto.getSupplier().getId(), savedLot.getSupplier().getId());
        assertEquals(lotDto.getMaterial().getId(), savedLot.getMaterial().getId());
    }

    @Test
    @Transactional
    void shouldReturnBadRequestWhenNameIsNull() throws Exception{
        LotDto lotDto = new LotDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lotDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn403WhenNotAuthenticated() throws Exception{
        SecurityContextHolder.clearContext();

        LotDto lotDto = new LotDto();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lotDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void shouldGetLotById() throws Exception{
        Lot lot = setUpLot("testLot", material, supplier);

        LotDto lotDto = lotMapper.mapToDto(lot);

        mockMvc.perform(get(URL + "/" + lot.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lotDto)));
    }

    @Test
    @Transactional
    void shouldGetAllLots() throws Exception{
        setUpLot("testLot", material, supplier);
        setUpLot("testLot2", material, supplier);

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
    void shouldUpdateLot() throws Exception{
        Lot lot = setUpLot("testLot", material, supplier);

        LotDto lotDto = lotMapper.mapToDto(lot);
        lotDto.setLotNumber("UpdatedTestLot");

        mockMvc.perform(put(URL + "/" + lot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lotDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        Optional<Lot> savedLot =
                lotRepository.findById(lot.getId());

        assertTrue(savedLot.isPresent());
        assertEquals(lotDto.getLotNumber(), savedLot.get().getLotNumber());
    }

    @Test
    @Transactional
    void shouldDeleteLotById() throws Exception{
        Lot lot = setUpLot("testLot2",material, supplier);

        mockMvc.perform(delete(URL + "/" + lot.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
