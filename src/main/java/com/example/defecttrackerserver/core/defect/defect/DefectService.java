package com.example.defecttrackerserver.core.defect.defect;

import java.util.List;

public interface DefectService {
    DefectDto saveDefect(DefectDto defectDto);
    DefectDto getDefectById(Integer id);
    List<DefectDto> getFilteredDefects(
            List<Integer> lotIds,
            List<Integer> defectStatusIds,
            String startDate,
            String endDate,
            List<Integer> locationIds,
            List<Integer> processIds,
            List<Integer> defectTypeIds,
            List<Integer> createdByIds
            );
    List<DefectDto> getAllDefects();
    DefectDto updateDefect(DefectDto defectDto);
    void deleteDefect(Integer id);
}
