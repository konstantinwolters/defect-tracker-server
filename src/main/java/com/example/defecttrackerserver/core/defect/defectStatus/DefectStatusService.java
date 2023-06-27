package com.example.defecttrackerserver.core.defect.defectStatus;

import java.util.Set;

public interface DefectStatusService {
    DefectStatusDto saveDefectStatus(DefectStatusDto defectStatusDto);
    DefectStatusDto getDefectStatusById(Integer id);
    Set<DefectStatusDto> getAllDefectStatus();
    DefectStatusDto updateDefectStatus(DefectStatusDto defectStatusDto);
    void deleteDefectStatus(Integer id);
}
