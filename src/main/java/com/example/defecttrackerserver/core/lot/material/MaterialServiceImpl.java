package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.core.lot.material.materialException.MaterialExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_ADMIN')")
    public MaterialDto saveMaterial(MaterialDto materialDto) {
        if(materialRepository.findByName(materialDto.getName()).isPresent())
            throw new MaterialExistsException("Material already exists with name: " + materialDto.getName());

        Material newMaterial = materialMapper.map(materialDto, new Material());

        Material savedMaterial = materialRepository.save(newMaterial);

        return materialMapper.mapToDto(savedMaterial);
    }

    @Override
    public MaterialDto getMaterialById(Integer id) {
        return materialRepository.findById(id)
                .map(materialMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + id));
    }

    @Override
    public List<MaterialDto> getAllMaterials() {
        return materialRepository.findAll()
                .stream()
                .map(materialMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_ADMIN')")
    public MaterialDto updateMaterial(Integer materialId, MaterialDto materialDto) {
        if(materialDto.getId() == null)
            throw new IllegalArgumentException("Material id must not be null");

        Material material = materialRepository.findById(materialId)
                .orElseThrow(()-> new EntityNotFoundException("Material not found with id: "
                        + materialId));

        Optional<Material> materialExists = materialRepository.findByName(materialDto.getName());
        if(materialExists.isPresent() && !materialExists.get().getId().equals(material.getId()))
            throw new MaterialExistsException("Material already exists with name: " + materialDto.getName());

        Material updatedMaterial = materialMapper.map(materialDto, material);
        Material savedMaterial = materialRepository.save(updatedMaterial);

        return materialMapper.mapToDto(savedMaterial);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteMaterial(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Material not found with id: " + id));

        materialRepository.delete(material);
    }
}
