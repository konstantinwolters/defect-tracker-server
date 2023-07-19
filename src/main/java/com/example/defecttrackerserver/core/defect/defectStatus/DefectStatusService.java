package com.example.defecttrackerserver.core.defect.defectStatus;

import java.util.List;

public interface DefectStatusService {
    DefectStatusDto saveDefectStatus(DefectStatusDto defectStatusDto);
    DefectStatusDto getDefectStatusById(Integer id);
    List<DefectStatusDto> getAllDefectStatus();
    DefectStatusDto updateDefectStatus(Integer defectStatusId, DefectStatusDto defectStatusDto);
    void deleteDefectStatus(Integer id);
}
