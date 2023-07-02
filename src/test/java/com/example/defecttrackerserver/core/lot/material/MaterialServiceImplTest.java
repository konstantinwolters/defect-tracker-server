package com.example.defecttrackerserver.core.lot.material;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaterialServiceImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private MaterialMapper materialMapper;

    @InjectMocks
    private MaterialServiceImpl materialService;

    private MaterialDto materialDto;
    private Material material;

    @BeforeEach
    void setUp() {
        materialDto = new MaterialDto();
        materialDto.setId(1);
        materialDto.setName("testName");

        material = new Material();
        material.setId(1);
        material.setName("testName");
    }

    @Test
    void shouldSaveMaterial() {
        when(materialRepository.save(any(Material.class))).thenReturn(material);
        when(materialMapper.mapToDto(any(Material.class))).thenReturn(materialDto);

        MaterialDto result = materialService.saveMaterial(materialDto);

        assertNotNull(result);
        assertEquals(material.getName(), result.getName());
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void shouldReturnMaterialById() {
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.of(material));
        when(materialMapper.mapToDto(material)).thenReturn(materialDto);

        MaterialDto result = materialService.getMaterialById(1);

        assertNotNull(result);
        assertEquals(material.getId(), result.getId());
        assertEquals(material.getName(), result.getName());
    }

    @Test
    void shouldReturnAllMaterials(){
        when(materialRepository.findAll()).thenReturn(Arrays.asList(material));
        when(materialMapper.mapToDto(material)).thenReturn(materialDto);

        List<MaterialDto> result = materialService.getAllMaterials();

        assertNotNull(result);
        assertEquals(material.getId(), result.get(0).getId());
        assertEquals(material.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateMaterial() {
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.of(material));
        when(materialRepository.save(any(Material.class))).thenReturn(material);
        when(materialMapper.mapToDto(any(Material.class))).thenReturn(materialDto);

        MaterialDto result = materialService.updateMaterial(materialDto);

        assertNotNull(result);
        assertEquals(material.getId(), result.getId());
        assertEquals(material.getName(), result.getName());
        verify(materialRepository, times(1)).save(material);
    }

    @Test
    void shouldDeleteMaterial() {
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.of(material));

        materialService.deleteMaterial(1);

        verify(materialRepository, times(1)).delete(material);
    }

    @Test
    void shouldThrowExceptionWhenMaterialNotFound(){
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> materialService.deleteMaterial(1));
    }
}
