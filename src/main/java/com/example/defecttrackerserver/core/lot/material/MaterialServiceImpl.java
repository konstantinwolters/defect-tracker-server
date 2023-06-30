package com.example.defecttrackerserver.core.lot.material;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final ModelMapper modelMapper;

    @Override
    public MaterialDto saveMaterial(MaterialDto materialDto) {
        if(materialDto.getName() == null)
            throw new IllegalArgumentException("Material name must not be null");

        Material material = new Material();
        material.setName(materialDto.getName());

        Material savedMaterial = materialRepository.save(material);

        return modelMapper.map(savedMaterial, MaterialDto.class);
    }

    @Override
    public MaterialDto getMaterialById(Integer id) {
        return materialRepository.findById(id)
                .map(material -> modelMapper.map(material, MaterialDto.class))
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + id));
    }

    @Override
    public List<MaterialDto> getAllMaterials() {
        return materialRepository.findAll()
                .stream()
                .map(material -> modelMapper.map(material, MaterialDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MaterialDto updateMaterial(MaterialDto materialDto) {
        if(materialDto.getId() == null)
            throw new IllegalArgumentException("Material id must not be null");
        if(materialDto.getName() == null)
            throw new IllegalArgumentException("Material name must not be null");

        Material material = materialRepository.findById(materialDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Material not found with id: "
                        + materialDto.getId()));

        material.setName(materialDto.getName());
        Material savedMaterial = materialRepository.save(material);

        return modelMapper.map(savedMaterial, MaterialDto.class);
    }

    @Override
    public void deleteMaterial(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Material not found with id: " + id));

        materialRepository.delete(material);
    }
}
