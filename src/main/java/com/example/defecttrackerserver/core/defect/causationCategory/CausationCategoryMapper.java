package com.example.defecttrackerserver.core.defect.causationCategory;

import org.springframework.stereotype.Component;

@Component
public class CausationCategoryMapper {
    public CausationCategoryDto mapToDto(CausationCategory causationCategory) {
        CausationCategoryDto causationCategoryDto = new CausationCategoryDto();
        causationCategoryDto.setId(causationCategory.getId());
        causationCategoryDto.setName(causationCategory.getName());
        return causationCategoryDto;
    }
}
