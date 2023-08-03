package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DefectService {
    DefectDto saveDefect(DefectDto defectDto);
    DefectDto getDefectById(Integer id);
    PaginatedResponse<DefectDto> getDefects(
            List<Integer> lotIds,
            List<Integer> materialsIds,
            List<Integer> suppliersIds,
            List<Integer> defectStatusIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            List<Integer> locationIds,
            List<Integer> processIds,
            List<Integer> defectTypeIds,
            List<Integer> createdByIds,
            List<Integer> changedByIds,
            Pageable pageable
            );
    DefectFilterValues getDefectFilterValues(List<Defect> defects);
    DefectDto updateDefect(Integer defectId, DefectDto defectDto);
    void deleteDefect(Integer id);
}
