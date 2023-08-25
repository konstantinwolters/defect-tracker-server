package com.example.defecttrackerserver.core.defect.causationCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CausationCategoryMapperTest {

    @InjectMocks
    private CausationCategoryMapper causationCategoryMapper;

    private CausationCategory causationCategory;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        causationCategory  = new CausationCategory();
        causationCategory.setId(1);
        causationCategory.setName("Test");
    }

    @Test
    void shouldReturnMappedCausationCategoryDto() {

        CausationCategoryDto mappedCausationCategory = causationCategoryMapper.mapToDto(causationCategory);

        assertEquals(causationCategory.getId(), mappedCausationCategory.getId());
        assertEquals(causationCategory.getName(), mappedCausationCategory.getName());
    }
}

