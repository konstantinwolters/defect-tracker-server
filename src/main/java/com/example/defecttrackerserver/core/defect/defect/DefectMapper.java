package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentMapper;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageMapper;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefectMapper {
    private final DefectStatusRepository defectStatusRepository;
    private final DefectCommentRepository defectCommentRepository;
    private final LotRepository lotRepository;
    private final LocationRepository locationRepository;
    private final ProcessRepository processRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final DefectImageRepository defectImageRepository;
    private final ActionRepository actionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DefectCommentMapper defectCommentMapper;
    private final DefectImageMapper defectImageMapper;
    private final ActionMapper actionMapper;

    public Defect map (DefectDto defectDto, Defect defect){
        DefectStatus defectStatus = getDefectStatusByName(defectDto.getDefectStatus());
        Location location = getLocationByName(defectDto.getLocation());
        Process process = getProcessByName(defectDto.getProcess());
        DefectType defectType = getDefectTypeByName(defectDto.getDefectType());
        User createdBy = getUserById(defectDto.getCreatedBy().getId());
        User changedBy = defectDto.getChangedBy() != null ? getUserById(defectDto.getChangedBy().getId()) : null;

        if(defectDto.getDefectComments() != null && !defectDto.getDefectComments().isEmpty()){
            Set<DefectComment> defectComments = defectDto.getDefectComments().stream()
                    .map(defectComment -> getDefectCommentById(defectComment.getId()))
                    .collect(Collectors.toSet());
            defect.getDefectComments().clear();
            defect.getDefectComments().addAll(defectComments);
        }

        if(defectDto.getImages() != null && !defectDto.getImages().isEmpty()){
            Set<DefectImage> defectImages = defectDto.getImages().stream()
                    .map(defectImage -> getDefectImageById(defectImage.getId()))
                    .collect(Collectors.toSet());
            defect.getImages().clear();
            defect.getImages().addAll(defectImages);
        }

        if(defectDto.getActions() != null && !defectDto.getActions().isEmpty()){
            Set<Action> actions = defectDto.getActions().stream()
                    .map(action -> getActionById(action.getId()))
                    .collect(Collectors.toSet());
            defect.getActions().clear();
            defect.getActions().addAll(actions);
            actions.forEach(action -> action.setDefect(defect));
        }

        defect.setCreatedAt(defectDto.getCreatedAt());
        defect.setChangedAt(defectDto.getChangedAt());
        defect.setDescription(defectDto.getDescription());
        defect.setDefectStatus(defectStatus);
        defect.setLocation(location);
        defect.setProcess(process);
        defect.setDefectType(defectType);
        defect.setCreatedBy(createdBy);
        defect.setChangedBy(changedBy);

        //Maintain relationships
        Lot lot = getLotByLotNumber(defectDto.getLot());
        lot.addDefect(defect);

        return defect;
    }

    private User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private Action getActionById(Integer id) {
        return actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));
    }

    private DefectImage getDefectImageById(Integer id) {
        return defectImageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect image not found with id: " + id));
    }

    private DefectType getDefectTypeByName(String name) {
        return defectTypeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Defect type not found with name: "
                        + name));
    }

    private Process getProcessByName(String name) {
        return processRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Process not found with name: "
                        + name));
    }

    private DefectStatus getDefectStatusByName (String name){
        return defectStatusRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Defect status not found with name: "
                        + name));
    }

    private DefectComment getDefectCommentById(Integer id){
        return defectCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect comment not found with id: " + id));
    }

    private Lot getLotByLotNumber(String lotNumber){
        return lotRepository.findByLotNumber(lotNumber)
                .orElseThrow(() -> new EntityNotFoundException("Lot not found with number: "
                        + lotNumber));
    }

    private Location getLocationByName(String name){
        return locationRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with name: "
                        + name));
    }

    public DefectDto mapToDto(Defect defect){
        UserDto createdBy = userMapper.mapToDto(defect.getCreatedBy());
        UserDto changedBy = defect.getChangedBy() != null ? userMapper.mapToDto(defect.getChangedBy()) : null;

        Set<DefectCommentDto> defectComments = Optional.ofNullable(defect.getDefectComments())
                .orElse(Collections.emptySet())
                .stream()
                .map(defectCommentMapper::mapToDto)
                .collect(Collectors.toSet());

        Set<DefectImageDto> defectImages = Optional.ofNullable(defect.getImages())
                .orElse(Collections.emptySet())
                .stream()
                .map(defectImageMapper::mapToDto)
                .collect(Collectors.toSet());

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
        defectDto.setDefectComments(defectComments);
        defectDto.setLot(defect.getLot().getLotNumber());
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
