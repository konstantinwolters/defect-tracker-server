package com.example.defecttrackerserver.core.defect.defectStatus;

import java.util.List;

/**
 * Service interface for managing {@link DefectStatus}.
 */
public interface DefectStatusService {
    DefectStatusDto saveDefectStatus(DefectStatusDto defectStatusDto);
    DefectStatusDto getDefectStatusById(Integer id);
    List<DefectStatusDto> getAllDefectStatus();
    DefectStatusDto updateDefectStatus(Integer defectStatusId, DefectStatusDto defectStatusDto);
    void deleteDefectStatus(Integer id);
}
