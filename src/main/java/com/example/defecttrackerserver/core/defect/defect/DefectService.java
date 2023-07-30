package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DefectService {
    DefectDto saveDefect(DefectDto defectDto);
    DefectDto getDefectById(Integer id);
    PaginatedResponse<DefectDto> getDefects(
            List<Integer> lotIds,
            List<Integer> defectStatusIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            List<Integer> locationIds,
            List<Integer> processIds,
            List<Integer> defectTypeIds,
            List<Integer> createdByIds,
            Pageable pageable
            );
    DefectFilterValues getDefectFilterValues(List<Defect> defects);
    DefectDto updateDefect(Integer defectId, DefectDto defectDto);
    void deleteDefect(Integer id);
}
