package com.example.defecttrackerserver.core.defect.defectStatus;

import java.util.List;
import java.util.Set;

public interface DefectStatusService {
    DefectStatusDto saveDefectStatus(DefectStatusDto defectStatusDto);
    DefectStatusDto getDefectStatusById(Integer id);
    List<DefectStatusDto> getAllDefectStatus();
    DefectStatusDto updateDefectStatus(DefectStatusDto defectStatusDto);
    void deleteDefectStatus(Integer id);
}
