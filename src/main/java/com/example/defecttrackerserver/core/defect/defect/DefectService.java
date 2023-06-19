package com.example.defecttrackerserver.core.defect.defect;

import java.util.List;

public interface DefectService {
    DefectDto saveDefect(DefectDto defectDto);
    DefectDto getDefectById(Integer id);
    List<DefectDto> getAllDefects();
    DefectDto updateDefect(DefectDto defectDto);
    void deleteDefect(Integer id);
}
