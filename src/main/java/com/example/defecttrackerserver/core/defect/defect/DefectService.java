package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DefectService {
    DefectDto saveDefect(DefectDto defectDto);
    DefectDto getDefectById(Integer id);
    List<DefectDto> getAllDefects();
    PaginatedResponse<DefectDto> getFilteredDefects(
            List<Integer> lotIds,
            List<Integer> defectStatusIds,
            String startDate,
            String endDate,
            List<Integer> locationIds,
            List<Integer> processIds,
            List<Integer> defectTypeIds,
            List<Integer> createdByIds,
            Pageable pageable
            );
    DefectFilterValues getDefectFilterValues(List<Defect> defects);
    DefectDto updateDefect(DefectDto defectDto);
    void deleteDefect(Integer id);
}
