package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private SupplierDto supperDto;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supperDto = new SupplierDto();
        supperDto.setId(1);
        supperDto.setName("testName");

        supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("testName");
    }

    @Test
    void shouldSaveSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(modelMapper.map(any(Supplier.class), eq(SupplierDto.class))).thenReturn(supperDto);

        SupplierDto result = supplierService.saveSupplier(supperDto);

        assertNotNull(result);
        assertEquals(supplier.getName(), result.getName());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void shouldReturnSupplierById() {
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.of(supplier));
        when(modelMapper.map(supplier, SupplierDto.class)).thenReturn(supperDto);

        SupplierDto result = supplierService.getSupplierById(1);

        assertNotNull(result);
        assertEquals(supplier.getId(), result.getId());
        assertEquals(supplier.getName(), result.getName());
    }

    @Test
    void shouldReturnAllSuppliers(){
        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier));
        when(modelMapper.map(supplier, SupplierDto.class)).thenReturn(supperDto);

        List<SupplierDto> result = supplierService.getAllSuppliers();

        assertNotNull(result);
        assertEquals(supplier.getId(), result.get(0).getId());
        assertEquals(supplier.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateSupplier() {
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(modelMapper.map(any(Supplier.class), eq(SupplierDto.class))).thenReturn(supperDto);

        SupplierDto result = supplierService.updateSupplier(supperDto);

        assertNotNull(result);
        assertEquals(supplier.getId(), result.getId());
        assertEquals(supplier.getName(), result.getName());
        verify(supplierRepository, times(1)).save(supplier);
    }

    @Test
    void shouldDeleteSupplier() {
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(1);

        verify(supplierRepository, times(1)).delete(supplier);
    }

    @Test
    void shouldThrowExceptionWhenSupplierNotFound(){
        when(supplierRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> supplierService.deleteSupplier(1));
    }
}
