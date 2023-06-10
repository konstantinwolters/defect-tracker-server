package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectImage.defectImage;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.user.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DefectDto {

    private Integer id;
    private DefectStatus defectStatus;
    private List<DefectComment> defectComments = new ArrayList<>();
    private Lot lot;
    private Location location;
    private Process process;
    private DefectType defectType;
    private List<defectImage> images = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private User createdBy;
}
