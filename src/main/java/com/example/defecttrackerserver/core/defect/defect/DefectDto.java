package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.lot.lot.LotDto;
import com.example.defecttrackerserver.core.user.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class DefectDto {

    private Integer id;
    private String description;
    private String defectStatus;
    private Set<DefectCommentDto> defectComments;
    private String lot;
    private String location;
    private String process;
    private String defectType;
    private Set<DefectImageDto> images;
    private Set<ActionDto> actions;
    private UserDto createdBy;
    private LocalDateTime createdOn;
}
