package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.material.MaterialMapper;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import com.example.defecttrackerserver.core.lot.supplier.SupplierMapper;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class LotMapperTest {

    @InjectMocks
    private LotMapper lotMapper;

    @Mock
    private EntityService entityService;

    @Mock
    private SupplierMapper supplierMapper;

    @Mock
    private MaterialMapper materialMapper;

    LotDto lotDto;
    Lot lot;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        MaterialDto materialDto = TestHelper.setUpMaterialDto();
        SupplierDto supplierDto = TestHelper.setUpSupplierDto();

        lotDto = TestHelper.setUpLotDto();
        lotDto.setMaterial(materialDto);
        lotDto.setSupplier(supplierDto);
        lot = TestHelper.setUpLot();
    }

    @Test
    void shouldReturnMappedLot() {
        Material material = TestHelper.setUpMaterial();
        Supplier supplier = TestHelper.setUpSupplier();
        Defect defect = TestHelper.setUpDefect();

        when(entityService.getMaterialById(any(Integer.class))).thenReturn(material);
        when(entityService.getSupplierById(any(Integer.class))).thenReturn(supplier);
        when(entityService.getDefectById(any(Integer.class))).thenReturn(defect);

        Lot mappedLot = lotMapper.map(lotDto, new Lot());

        assertEquals(lotDto.getMaterial().getId(), mappedLot.getMaterial().getId());
        assertEquals(lotDto.getSupplier().getId(), mappedLot.getSupplier().getId());
        assertEquals(lotDto.getDefects().size(), mappedLot.getDefects().size());
    }

    @Test
    void shouldReturnMappedLotDto() {
        when(supplierMapper.mapToDto(any(Supplier.class))).thenReturn(new SupplierDto());
        when(materialMapper.mapToDto(any(Material.class))).thenReturn(new MaterialDto());

        LotDto mappedLotDto = lotMapper.mapToDto(lot);

        assertEquals(lot.getId(), mappedLotDto.getId());
        assertEquals(lot.getLotNumber(), mappedLotDto.getLotNumber());
        assertEquals(lot.getDefects().size(), mappedLotDto.getDefects().size());
    }
}

