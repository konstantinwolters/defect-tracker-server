package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class MaterialMapper {
    private final UserMapper userMapper;
    private final EntityService entityService;

    public Material map(MaterialDto materialDto, Material material ){
        Set<User> responsibleUsers = materialDto.getResponsibleUsers().stream()
                        .map(user -> entityService.getUserById(user.getId()))
                                .collect(Collectors.toSet());

        material.setName(materialDto.getName());
        material.setCustomId(material.getCustomId());
        material.setResponsibleUsers(responsibleUsers);

        return  material;
    }

    public MaterialDto mapToDto(Material material) {
        Set<UserDto> responsibleUsers = material.getResponsibleUsers().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toSet());

        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(material.getId());
        materialDto.setCustomId(material.getCustomId());
        materialDto.setName(material.getName());
        materialDto.setResponsibleUsers(responsibleUsers);
        return materialDto;
    }
}
