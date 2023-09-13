package com.example.defecttrackerserver.core.lot.supplier;

import com.example.defecttrackerserver.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplierMapperTest {

    @InjectMocks
    private SupplierMapper supplierMapper;

    private Supplier supplier;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        supplier = TestHelper.setUpSupplier();
    }

    @Test
    void shouldReturnMappedSupplierDto() {

        SupplierDto mappedSupplier = supplierMapper.mapToDto(supplier);

        assertEquals(supplier.getId(), mappedSupplier.getId());
        assertEquals(supplier.getName(), mappedSupplier.getName());
    }
}

