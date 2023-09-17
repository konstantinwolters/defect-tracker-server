package com.example.defecttrackerserver.core.lot.material;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link MaterialService}.
 */
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_QA') or hasRole('ROLE_ADMIN') ")
    public MaterialDto saveMaterial(MaterialDto materialDto) {
        if(materialRepository.findByName(materialDto.getName()).isPresent())
            throw new DuplicateKeyException("Material already exists with name: " + materialDto.getName());

        Material newMaterial = materialMapper.map(materialDto, new Material());

        Material savedMaterial = materialRepository.save(newMaterial);

        return materialMapper.mapToDto(savedMaterial);
    }

    @Override
    public MaterialDto getMaterialById(Integer id) {
        Material material = findMaterialById(id);
        return materialMapper.mapToDto(material);
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
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public MaterialDto updateMaterial(Integer materialId, MaterialDto materialDto) {
        Material material = findMaterialById(materialId);

        Optional<Material> materialExists = materialRepository.findByName(materialDto.getName());
        if(materialExists.isPresent() && !materialExists.get().getId().equals(material.getId()))
            throw new DuplicateKeyException("Material already exists with name: " + materialDto.getName());

        Material updatedMaterial = materialMapper.map(materialDto, material);
        Material savedMaterial = materialRepository.save(updatedMaterial);

        return materialMapper.mapToDto(savedMaterial);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteMaterial(Integer id) {
        Material material = findMaterialById(id);
        materialRepository.delete(material);
    }

    private Material findMaterialById(Integer id){
        return materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));
    }
}
