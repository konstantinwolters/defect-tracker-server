package com.example.defecttrackerserver.core.lot.supplier;

import com.example.defecttrackerserver.BaseControllerTest;
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

@WebMvcTest(SupplierController.class)
public class SupplierControllerTest extends BaseControllerTest {

    @Autowired
    private SupplierController supplierController;

    @MockBean
    private SupplierServiceImpl supplierService;

    private SupplierDto testSupplierDto;

    @Override
    protected Object getController() {
        return supplierController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testSupplierDto = new SupplierDto();
        testSupplierDto.setName("testName");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveSupplier() throws Exception {
        when(supplierService.saveSupplier(any(SupplierDto.class))).thenReturn(testSupplierDto);

        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSupplierDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetSupplierById() throws Exception {
        when(supplierService.getSupplierById(any(Integer.class))).thenReturn(testSupplierDto);

        mockMvc.perform(get("/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllSuppliers() throws Exception {
        when(supplierService.getAllSuppliers()).thenReturn(Arrays.asList(testSupplierDto));

        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateSupplier() throws Exception {
        when(supplierService.updateSupplier(any(Integer.class),any(SupplierDto.class))).thenReturn(testSupplierDto);
        mockMvc.perform(put("/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSupplierDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteSupplier() throws Exception {
        doNothing().when(supplierService).deleteSupplier(any(Integer.class));
        mockMvc.perform(delete("/suppliers/1"))
                .andExpect(status().isOk());
    }
}
