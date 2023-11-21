package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentMapper;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageMapper;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotMapper;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class DefectMapper {
    private final EntityService entityService;
    private final UserMapper userMapper;
    private final LotMapper lotMapper;
    private final DefectCommentMapper defectCommentMapper;
    private final DefectImageMapper defectImageMapper;
    private final ActionMapper actionMapper;

    public Defect map (DefectDto defectDto, Defect defect){
        DefectStatus defectStatus = defectDto.getDefectStatus() != null ?
                entityService.getDefectStatusByName(defectDto.getDefectStatus()) : null;

        CausationCategory causationCategory = defectDto.getCausationCategory() != null ?
                entityService.getCausationCategoryByName(defectDto.getCausationCategory()) : null;

        DefectType defectType = defectDto.getDefectType() != null ?
                entityService.getDefectTypeByName(defectDto.getDefectType()) : null;

        Location location = entityService.getLocationByName(defectDto.getLocation());
        Process process = entityService.getProcessByName(defectDto.getProcess());
        User createdBy = entityService.getUserById(defectDto.getCreatedBy().getId());
        User changedBy = defectDto.getChangedBy() != null ? entityService.getUserById(defectDto.getChangedBy().getId()) : null;

        if(defectDto.getDefectComments() != null){
            Set<DefectComment> defectComments = defectDto.getDefectComments().stream()
                    .map(defectComment -> entityService.getDefectCommentById(defectComment.getId()))
                    .collect(Collectors.toSet());
            defect.getDefectComments().clear();
            defect.getDefectComments().addAll(defectComments);
        }

        if(defectDto.getImages() != null){
            Set<DefectImage> defectImages = defectDto.getImages().stream()
                    .map(defectImage -> entityService.getDefectImageById(defectImage.getId()))
                    .collect(Collectors.toSet());
            defect.getImages().clear();
            defect.getImages().addAll(defectImages);
        }

        if(defectDto.getActions() != null){
            Set<Action> actions = defectDto.getActions().stream()
                    .map(action -> entityService.getActionById(action.getId()))
                    .collect(Collectors.toSet());
            defect.getActions().clear();
            defect.getActions().addAll(actions);
            actions.forEach(action -> action.setDefect(defect));
        }

        defect.setCreatedAt(defectDto.getCreatedAt());
        defect.setChangedAt(defectDto.getChangedAt());
        defect.setDescription(defectDto.getDescription());
        defect.setDefectStatus(defectStatus);
        defect.setCausationCategory(causationCategory);
        defect.setLocation(location);
        defect.setProcess(process);
        defect.setDefectType(defectType);
        defect.setCreatedBy(createdBy);
        defect.setChangedBy(changedBy);

        //Maintain relationships
        Lot lot = entityService.getLotById(defectDto.getLot().getId());
        lot.addDefect(defect);

        return defect;
    }

    public DefectDto mapToDto(Defect defect){
        UserDto createdBy = userMapper.mapToDto(defect.getCreatedBy());
        UserDto changedBy = defect.getChangedBy() != null ? userMapper.mapToDto(defect.getChangedBy()) : null;
        LotDto lot = lotMapper.mapToDto(defect.getLot());

        Set<DefectCommentDto> defectComments = Optional.ofNullable(defect.getDefectComments())
                .orElse(Collections.emptySet())
                .stream()
                .map(defectCommentMapper::mapToDto)
                .collect(Collectors.toSet());

        List<DefectImageDto> defectImages = Optional.ofNullable(defect.getImages())
                .orElse(Collections.emptyList())
                .stream()
                .map(defectImageMapper::mapToDto)
                .collect(Collectors.toList());

        Set<ActionDto> actions = Optional.ofNullable(defect.getActions())
                .orElse(Collections.emptySet())
                .stream()
                .map(actionMapper::mapToDto)
                .collect(Collectors.toSet());

        DefectDto defectDto = new DefectDto();
        defectDto.setId(defect.getId());
        defectDto.setDescription(defect.getDescription());
        defectDto.setCreatedAt(defect.getCreatedAt());
        defectDto.setChangedAt(defect.getChangedAt());
        defectDto.setDefectStatus(defect.getDefectStatus().getName());
        defectDto.setCausationCategory(defect.getCausationCategory().getName());
        defectDto.setDefectComments(defectComments);
        defectDto.setLot(lot);
        defectDto.setLocation(defect.getLocation().getName());
        defectDto.setProcess(defect.getProcess().getName());
        defectDto.setDefectType(defect.getDefectType().getName());
        defectDto.setImages(defectImages);
        defectDto.setActions(actions);
        defectDto.setCreatedBy(createdBy);
        defectDto.setChangedBy(changedBy);

        return defectDto;
    }
}
