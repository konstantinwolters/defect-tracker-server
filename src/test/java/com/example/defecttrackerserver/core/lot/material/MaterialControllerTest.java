package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaterialController.class)
public class MaterialControllerTest extends BaseControllerTest {

    @Autowired
    private MaterialController materialController;

    @MockBean
    private MaterialServiceImpl materialService;

    private MaterialDto testMaterialDto;

    @Override
    protected Object getController() {
        return materialController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testMaterialDto = new MaterialDto();
        testMaterialDto.setName("testName");
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveMaterial() throws Exception {
        when(materialService.saveMaterial(any(MaterialDto.class))).thenReturn(testMaterialDto);

        mockMvc.perform(post("/materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMaterialDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetMaterialById() throws Exception {
        when(materialService.getMaterialById(any(Integer.class))).thenReturn(testMaterialDto);

        mockMvc.perform(get("/materials/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "XXXX", roles = "ADMIN")
    public void shouldGetAllMaterials() throws Exception {
        when(materialService.getAllMaterials()).thenReturn(Arrays.asList(testMaterialDto));

        mockMvc.perform(get("/materials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateMaterial() throws Exception {
        when(materialService.updateMaterial(any(MaterialDto.class))).thenReturn(testMaterialDto);
        mockMvc.perform(put("/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMaterialDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteMaterial() throws Exception {
        doNothing().when(materialService).deleteMaterial(any(Integer.class));
        mockMvc.perform(delete("/materials/1"))
                .andExpect(status().isOk());
    }
}
