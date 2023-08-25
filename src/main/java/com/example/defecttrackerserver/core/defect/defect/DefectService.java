package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.response.PaginatedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface DefectService {
    DefectDto saveDefect(DefectDto defectDto, MultipartFile[] images);
    DefectDto getDefectById(Integer id);
    PaginatedResponse<DefectDto> getDefects(
            String search,
            String lotIds,
            String materialsIds,
            String suppliersIds,
            String defectStatusIds,
            String causationCategoryIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            String locationIds,
            String processIds,
            String defectTypeIds,
            String createdByIds,
            String changedByIds,
            Integer page,
            Integer size,
            String sort
            );
    DefectFilterValues getDefectFilterValues(List<Defect> defects);
    DefectDto updateDefect(Integer defectId, DefectDto defectDto, MultipartFile[] images);
    void deleteDefect(Integer id);
}
