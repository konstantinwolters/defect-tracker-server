package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LotController.class)
public class LotControllerTest extends BaseControllerTest{

    @Autowired
    private LotController lotController;

    @MockBean
    private LotServiceImpl lotService;

    private LotDto testLotDto;

    @Override
    protected Object getController() {
        return lotController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        MaterialDto materialDto = TestHelper.setUpMaterialDto();
        SupplierDto supplierDto = TestHelper.setUpSupplierDto();

        testLotDto = TestHelper.setUpLotDto();
        testLotDto.setMaterial(materialDto);
        testLotDto.setSupplier(supplierDto);
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveLot() throws Exception {
        when(lotService.saveLot(any(LotDto.class))).thenReturn(testLotDto);

        mockMvc.perform(post("/lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLotDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.material.name").value("testMaterial"))
                .andExpect(jsonPath("$.supplier.name").value("testSupplier"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetLotById() throws Exception {
        when(lotService.getLotById(any(Integer.class))).thenReturn(testLotDto);

        mockMvc.perform(get("/lots/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.material.name").value("testMaterial"))
                .andExpect(jsonPath("$.supplier.name").value("testSupplier"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAllLots() throws Exception {
        when(lotService.getAllLots()).thenReturn(Arrays.asList(testLotDto));

        mockMvc.perform(get("/lots"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].material.name").value("testMaterial"))
                .andExpect(jsonPath("$[0].supplier.name").value("testSupplier"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateLot() throws Exception {
        when(lotService.updateLot(any(Integer.class),any(LotDto.class))).thenReturn(testLotDto);
        mockMvc.perform(put("/lots/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLotDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.material.name").value("testMaterial"))
                .andExpect(jsonPath("$.supplier.name").value("testSupplier"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteLot() throws Exception {
        doNothing().when(lotService).deleteLot(any(Integer.class));
        mockMvc.perform(delete("/lots/1"))
                .andExpect(status().isNoContent());
    }
}
