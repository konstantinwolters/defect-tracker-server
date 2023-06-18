package com.example.defecttrackerserver.core.defect.defect;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefectServiceImpl implements DefectService{

    @Override
    public DefectDto saveDefect(DefectDto defectDto) {
        Defect defect = new Defect();

        return null;
    }

    @Override
    public DefectDto getDefect(Integer id) {
        return null;
    }

    @Override
    public List<DefectDto> getAllDefects() {
        return null;
    }

    @Override
    public DefectDto updateDefect(DefectDto defectDto) {
        return null;
    }

    @Override
    public void deleteDefect(Integer id) {

    }
}
