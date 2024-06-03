package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryDto;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusDto;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeDto;
import com.example.defecttrackerserver.core.defect.process.ProcessDto;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.lot.lot.dto.LotInfo;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
/**
 *
 * Transfer object for distinct filter values for the endpoint /getFilteredDefects.
 * It is part of {@link com.example.defecttrackerserver.response.PaginatedResponse}.
 */

@Getter
@Setter
class DefectFilterValues {
    private Set<DefectStatusDto> defectStatuses;
    private Set<CausationCategoryDto> causationCategories;
    private Set<LotInfo> lots;
    private Set<MaterialDto> materials;
    private Set<SupplierDto> suppliers;
    private Set<LocationDto> locations;
    private Set<ProcessDto> processes;
    private Set<DefectTypeDto> defectTypes;
    private Set<UserInfo> createdByUsers;
    private Set<LocalDate> createdDates;
    private Set<UserInfo> changedByUsers;
    private Set<LocalDate> changedDates;
}
