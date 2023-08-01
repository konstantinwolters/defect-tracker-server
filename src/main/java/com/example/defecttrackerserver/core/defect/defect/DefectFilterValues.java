package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class DefectFilterValues {
    private Set<String> defectStatus;
    private Set<String> lot; //TODO: Lot Number must be filter value. LotDto as filter object?
    //TODO: Add filter by material
    //TODO: Add filter by supplier
    private Set<String> location;
    private Set<String> process;
    private Set<String> defectType;
    private Set<UserInfo> createdBy;
    private Set<LocalDate> createdAt;
    private Set<UserInfo> changedBy;
    private Set<LocalDate> changedAt;
}
