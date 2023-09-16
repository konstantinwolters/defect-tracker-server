package com.example.defecttrackerserver.core.defect.causationCategory;

import java.util.List;

/**
 * Service interface managing {@link CausationCategory}.
 */
public interface CausationCategoryService {
    CausationCategoryDto saveCausationCategory(CausationCategoryDto causationCategoryDto);
    CausationCategoryDto getCausationCategoryById(Integer id);
    List<CausationCategoryDto> getAllCausationCategories();
    CausationCategoryDto updateCausationCategory(Integer causationCategoryId, CausationCategoryDto causationCategoryDto);
    void deleteCausationCategory(Integer id);
}
