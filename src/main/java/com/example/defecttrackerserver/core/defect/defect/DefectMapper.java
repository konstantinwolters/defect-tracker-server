package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentMapper;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageMapper;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotMapper;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        defect.setCreatedOn(defectDto.getCreatedOn());
        defect.setDescription(defectDto.getDescription());
        defect.setDefectStatus(defectStatusRepository.findByName(defectDto.getDefectStatus())
                .orElseThrow(() -> new EntityNotFoundException("Defect status not found with name: "
                + defectDto.getDefectStatus())));

        if(defectDto.getDefectComments() != null){
            Set<DefectComment> defectComments = defectDto.getDefectComments().stream()
                    .map(defectComment -> defectCommentRepository.findById(defectComment.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Defect comment not found with id: "
                                    + defectComment.getId())))
                    .collect(Collectors.toSet());
            defect.getDefectComments().clear();
            defect.getDefectComments().addAll(defectComments);
        }

        Lot lot = lotRepository.findByLotNumber(defectDto.getLot())
                .orElseThrow(() -> new EntityNotFoundException("Lot not found with number: "
                        + defectDto.getLot()));
        lot.addDefect(defect);

        defect.setLocation(locationRepository.findByName(defectDto.getLocation())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: "
                        + defectDto.getLocation())));

        defect.setProcess(processRepository.findByName(defectDto.getProcess())
                .orElseThrow(() -> new EntityNotFoundException("Process not found with id: "
                        + defectDto.getProcess())));

        defect.setDefectType(defectTypeRepository.findByName(defectDto.getDefectType())
                .orElseThrow(() -> new EntityNotFoundException("Defect type not found with id: "
                        + defectDto.getDefectType())));

        if(defectDto.getImages() != null){
            Set<DefectImage> defectImages = defectDto.getImages().stream()
                    .map(defectImage -> defectImageRepository.findById(defectImage.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Defect image not found with id: "
                                    + defectImage.getId())))
                    .collect(Collectors.toSet());
            defect.getImages().clear();
            defect.getImages().addAll(defectImages);
        }

        if(defectDto.getActions() != null){
            Set<Action> actions = defectDto.getActions().stream()
                    .map(action -> actionRepository.findById(action.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Action not found with id: "
                                    + action.getId())))
                    .collect(Collectors.toSet());
            defect.getActions().clear();
            defect.getActions().addAll(actions);
            actions.forEach(action -> action.setDefect(defect));
        }

        defect.setCreatedBy(userRepository.findById(defectDto.getCreatedBy().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "
                        + defectDto.getCreatedBy().getId())));
        return defect;
    }

    public DefectDto mapToDto(Defect defect){
        DefectDto defectDto = new DefectDto();
        defectDto.setId(defect.getId());
        defectDto.setDescription(defect.getDescription());
        defectDto.setCreatedOn(defect.getCreatedOn());
        defectDto.setDefectStatus(defect.getDefectStatus().getName());
        defectDto.setDefectComments(defect.getDefectComments().stream()
                .map(defectCommentMapper::mapToDto)
                .collect(Collectors.toSet()));
        defectDto.setLot(defect.getLot().getLotNumber());
        defectDto.setLocation(defect.getLocation().getName());
        defectDto.setProcess(defect.getProcess().getName());
        defectDto.setDefectType(defect.getDefectType().getName());
        defectDto.setImages(defect.getImages().stream()
                .map(defectImageMapper::mapToDto)
                .collect(Collectors.toSet()));
        defectDto.setActions(defect.getActions().stream()
                .map(actionMapper::mapToDto)
                .collect(Collectors.toSet()));
        defectDto.setCreatedBy(
                userMapper.mapToDto(userRepository.findById(defect.getCreatedBy().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "
                        + defect.getCreatedBy().getId())))
        );
        return defectDto;
    }
}
