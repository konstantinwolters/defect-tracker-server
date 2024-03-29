package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class MaterialMapperTest {

    @InjectMocks
    private MaterialMapper materialMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EntityService entityService;

    private Material material;
    private MaterialDto materialDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        UserDto userDto = TestHelper.setUpUserDto();

        material = TestHelper.setUpMaterial();
        materialDto = TestHelper.setUpMaterialDto();
        materialDto.setResponsibleUsers(new HashSet<>(Set.of(userDto)));
    }

    @Test
    void shouldReturnMappedMaterial() {
        when(entityService.getUserById(anyInt())).thenReturn(new User());
        Material mappedMaterial = materialMapper.map(materialDto, material);

        assertEquals(material.getId(), mappedMaterial.getId());
        assertEquals(material.getName(), mappedMaterial.getName());
        assertEquals(material.getCustomId(), mappedMaterial.getCustomId());
        assertEquals(material.getResponsibleUsers(), mappedMaterial.getResponsibleUsers());
    }

    @Test
    void shouldReturnMappedMaterialDto() {
        when(userMapper.mapToDto(any(User.class))).thenReturn(new UserDto());

        MaterialDto materialDto = materialMapper.mapToDto(material);

        assertEquals(material.getId(), materialDto.getId());
        assertEquals(material.getName(), materialDto.getName());
        assertEquals(material.getCustomId(), materialDto.getCustomId());
        assertEquals(material.getResponsibleUsers().size(), materialDto.getResponsibleUsers().size());
    }
}

