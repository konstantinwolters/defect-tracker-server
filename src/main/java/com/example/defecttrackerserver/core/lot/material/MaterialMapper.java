package com.example.defecttrackerserver.core.lot.material;

import org.springframework.stereotype.Component;

@Component
public class MaterialMapper {
    public MaterialDto mapToDto(Material material) {
        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(material.getId());
        materialDto.setCustomId(material.getCustomId());
        materialDto.setName(material.getName());
        return materialDto;
    }
}
