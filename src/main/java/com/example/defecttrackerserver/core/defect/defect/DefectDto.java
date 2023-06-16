package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusDto;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeDto;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessDto;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotDto;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DefectDto {

    private Integer id;
    private String defectStatus;
    private List<DefectCommentDto> defectComments = new ArrayList<>();
    private LotDto lot;
    private String location;
    private String process;
    private String defectType;
    private List<String> images = new ArrayList<>();
    private List<ActionDto> actions = new ArrayList<>();
    private UserDto createdBy;
}
