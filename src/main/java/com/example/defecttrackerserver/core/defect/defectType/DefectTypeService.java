package com.example.defecttrackerserver.core.defect.defectType;

import java.util.List;

public interface DefectTypeService {
    DefectTypeDto saveDefectType(DefectTypeDto defectTypeDto);
    DefectTypeDto getDefectTypeById(Integer id);
    List<DefectTypeDto> getAllDefectTypes();
    DefectTypeDto updateDefectType(Integer defectTypeId, DefectTypeDto defectTypeDto);
    void deleteDefectType(Integer id);
}
