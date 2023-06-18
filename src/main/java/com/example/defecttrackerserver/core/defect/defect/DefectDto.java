package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.lot.lot.LotDto;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private List<DefectImageDto> images = new ArrayList<>();
    private List<ActionDto> actions = new ArrayList<>();
    private UserDto createdBy;
    private LocalDateTime createdOn;
}
