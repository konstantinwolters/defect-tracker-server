package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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


    public Defect map (DefectDto defectDto, Defect defect){
        checkNullOrEmptyFields(defectDto);

        defect.setCreatedOn(defectDto.getCreatedOn());
        defect.setDefectStatus(defectStatusRepository.findByName(defectDto.getDefectStatus())
                .orElseThrow(() -> new EntityNotFoundException("Defect status not found with name: "
                + defectDto.getDefectStatus())));

        if(defectDto.getDefectComments() == null){
            defect.setDefectComments(new HashSet<>());
        }else {
            Set<DefectComment> defectComments = defectDto.getDefectComments().stream()
                    .map(defectComment -> defectCommentRepository.findById(defectComment.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Defect comment not found with id: "
                                    + defectComment.getId())))
                    .collect(Collectors.toSet());
            defect.getDefectComments().clear();
            defect.getDefectComments().addAll(defectComments);
        }

        defect.setLot(lotRepository.findById(defectDto.getLot().getId())
                .orElseThrow(() -> new EntityNotFoundException("Lot not found with id: "
                        + defectDto.getLot().getId())));

        defect.setLocation(locationRepository.findByName(defectDto.getLocation())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: "
                        + defectDto.getLocation())));

        defect.setProcess(processRepository.findByName(defectDto.getProcess())
                .orElseThrow(() -> new EntityNotFoundException("Process not found with id: "
                        + defectDto.getProcess())));

        defect.setDefectType(defectTypeRepository.findByName(defectDto.getDefectType())
                .orElseThrow(() -> new EntityNotFoundException("Defect type not found with id: "
                        + defectDto.getDefectType())));

        if(defectDto.getImages() == null){
            defect.setImages(new HashSet<>());//TODO: is this really necessary? Defect class already as HashSet
        }else{
            Set<DefectImage> defectImages = defectDto.getImages().stream()
                    .map(defectImage -> defectImageRepository.findById(defectImage.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Defect image not found with id: "
                                    + defectImage.getId())))
                    .collect(Collectors.toSet());
            defect.getImages().clear();
            defect.getImages().addAll(defectImages);
        }

        if(defectDto.getActions() == null){
            defect.setActions(new HashSet<>());
        }else{
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

    public void checkNullOrEmptyFields(DefectDto defectDto) {
        if(defectDto.getDefectStatus() == null)
            throw new IllegalArgumentException("DefectStatus must not be null");

        if(defectDto.getCreatedOn() == null)
            throw new IllegalArgumentException("CreatedOn must not be null");

        if(defectDto.getLot() == null)
            throw new IllegalArgumentException("Lot must not be null");

        if(defectDto.getLocation() == null)
            throw new IllegalArgumentException("Location must not be null");

        if(defectDto.getProcess() == null)
            throw new IllegalArgumentException("Process must not be null");

        if(defectDto.getDefectType() == null)
            throw new IllegalArgumentException("DefectType must not be null");

        if(defectDto.getCreatedBy() == null)
            throw new IllegalArgumentException("CreatedBy must not be null");
    }
}
