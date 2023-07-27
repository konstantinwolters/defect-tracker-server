package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class DefectFilterValues {
    private Set<String> defectStatus;
    private Set<String> lot;
    private Set<String> location;
    private Set<String> process;
    private Set<String> defectType;
    private Set<UserInfo> createdBy;
    private Set<LocalDate> createdOn;
    //TODO: Add filter for changedAt and ChangedBy
}
