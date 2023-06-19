package com.example.defecttrackerserver.core.defect.defect;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final ModelMapper modelMapper;
    private final DefectMapper defectMapper;


    @Override
    public DefectDto saveDefect(DefectDto defectDto) {
        Defect defect = new Defect();
        defectMapper.checkNullOrEmptyFields(defectDto);

        Defect newDefect = defectMapper.map(defectDto, defect);
        newDefect.setCreatedOn(LocalDateTime.now());

        return modelMapper.map(defectRepository.save(newDefect), DefectDto.class);
    }

    @Override
    public DefectDto getDefect(Integer id) {
        return null;
    }

    @Override
    public List<DefectDto> getAllDefects() {
        return defectRepository.findAll().stream().map(defect -> modelMapper.map(defect, DefectDto.class)).toList();
    }

    @Override
    public DefectDto updateDefect(DefectDto defectDto) {
        return null;
    }

    @Override
    public void deleteDefect(Integer id) {

    }
}
