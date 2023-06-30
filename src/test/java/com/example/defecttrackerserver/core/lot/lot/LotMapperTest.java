package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class LotMapperTest {

    @InjectMocks
    private LotMapper lotMapper;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private DefectRepository defectRepository;

    LotDto lotDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        lotDto = new LotDto();
        lotDto.setId(1);

        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(1);
        lotDto.setMaterial(materialDto);

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(1);
        lotDto.setSupplier(supplierDto);

        lotDto.setDefects(Set.of(1));
    }

    @Test
    void shouldReturnMappedAction() {
        Material material = new Material();
        material.setId(1);

        Supplier supplier = new Supplier();
        supplier.setId(1);

        Defect defect = new Defect();
        defect.setId(1);

        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.of(material));
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.of(supplier));
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));

        Lot lot = new Lot();
        Lot mappedLot = lotMapper.map(lotDto, lot);

        assertEquals(lotDto.getMaterial().getId(), mappedLot.getMaterial().getId());
        assertEquals(lotDto.getSupplier().getId(), mappedLot.getSupplier().getId());
        assertEquals(lotDto.getDefects().size(), mappedLot.getDefects().size());
    }

    @Test
    void shouldThrowExceptionWhenMaterialNotFound() {
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lotMapper.map(lotDto, new Lot()));
    }

    @Test
    void shouldThrowExceptionWhenSupplierNotFound() {
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Material()));
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lotMapper.map(lotDto, new Lot()));
    }

    @Test
    void shouldThrowExceptionWhenDefectNotFound() {
        when(materialRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Material()));
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Supplier()));
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lotMapper.map(lotDto, new Lot()));
    }

    @Test
    void shouldThrowExceptionWhenNullOrEmptyFields() {
        lotDto.setMaterial(null);
        assertThrows(IllegalArgumentException.class, () -> lotMapper.checkNullOrEmptyFields(lotDto));

        lotDto.setMaterial(new MaterialDto());
        lotDto.setSupplier(null);
        assertThrows(IllegalArgumentException.class, () -> lotMapper.checkNullOrEmptyFields(lotDto));
    }
}

