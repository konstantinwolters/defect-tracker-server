package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.user.user.UserInfo;
import com.example.defecttrackerserver.response.PaginatedResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final DefectMapper defectMapper;

    @Override
    @Transactional
    public DefectDto saveDefect(DefectDto defectDto) {
        Defect defect = new Defect();
        defectDto.setId(null);
        defectDto.setCreatedOn(LocalDateTime.now());

        Defect newDefect = defectMapper.map(defectDto, defect);
        DefectStatus defectStatus = defectStatusRepository.findByName("New")
                .orElseThrow(()-> new EntityNotFoundException("DefectStatus not found with name: 'New'"));
        newDefect.setDefectStatus(defectStatus);

        return defectMapper.mapToDto(defectRepository.save(newDefect));
    }

    @Override
    public DefectDto getDefectById(Integer id) {
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));
        return defectMapper.mapToDto(defect);
    }

    @Override
   public PaginatedResponse<DefectDto> getDefects(
            List<Integer> lotIds,
            List<Integer> defectStatusIds,
            String startDate,
            String endDate,
            List<Integer> locationIds,
            List<Integer> processIds,
            List<Integer> defectTypeIds,
            List<Integer> createdByIds,
            Pageable pageable
    ){
        Specification<Defect> spec = Specification.where(null);

        if(lotIds != null && !lotIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("lot").get("id").in(lotIds));
        }

        if(defectStatusIds != null && !defectStatusIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("defectStatus").get("id").in(defectStatusIds));
        }

       if(startDate != null && endDate != null){
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
           LocalDate startDateObj = LocalDate.parse(startDate, formatter);
           LocalDate endDateObj = LocalDate.parse(endDate, formatter);
           LocalDateTime startOfDay = startDateObj.atStartOfDay();
           LocalDateTime endOfDay = endDateObj.atStartOfDay().plusDays(1).minusSeconds(1);

           spec = spec.and((root, query, cb) -> cb.between(root.get("createdOn"), startOfDay, endOfDay));
       }

        if(locationIds != null && !locationIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("location").get("id").in(locationIds));
        }

        if(processIds != null && !processIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("process").get("id").in(processIds));
        }

        if(defectTypeIds != null && !defectTypeIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("defectType").get("id").in(defectTypeIds));
        }

        if(createdByIds != null && !createdByIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("createdBy").get("id").in(createdByIds));
        }

        Page<Defect> defects = defectRepository.findAll(spec, pageable);
        List<DefectDto> defectDtos =  defects.stream().map(defectMapper::mapToDto).toList();

        List<Defect> filteredDefects = defectRepository.findAll(spec);

        return new PaginatedResponse<>(
                defectDtos,
                defects.getTotalPages(),
                (int) defects.getTotalElements(),
                defects.getNumber(),
                getDefectFilterValues(filteredDefects)
        );
    }

    @Override
    public DefectFilterValues getDefectFilterValues(List<Defect> defects) {
        List<Integer> defectIds = defects.stream().map(Defect::getId).toList();

        DefectFilterValues defectFilterValues = new DefectFilterValues();
        defectFilterValues.setLot(defectRepository.findDistinctLotNumber(defectIds));
        defectFilterValues.setLocation(defectRepository.findDistinctLocationName(defectIds));
        defectFilterValues.setProcess(defectRepository.findDistinctProcessName(defectIds));
        defectFilterValues.setDefectType(defectRepository.findDistinctDefectTypeName(defectIds));
        defectFilterValues.setCreatedBy(defectRepository.findDistinctCreatedBy(defectIds).stream()
                .map(UserInfo::new).collect(Collectors.toSet()));
        defectFilterValues.setDefectStatus(defectRepository.findDistinctDefectStatusName(defectIds));
        defectFilterValues.setCreatedOn(defectRepository.findDistinctCreatedOn(defectIds).stream()
                .map(LocalDateTime::toLocalDate).collect(Collectors.toSet()));
        return defectFilterValues;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectDto updateDefect(Integer defectId, DefectDto defectDto) {
        Defect defectToUpdate = defectRepository.findById(defectId)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + defectId));

        defectToUpdate.setDefectStatus(defectStatusRepository.findByName(defectDto.getDefectStatus())
                .orElseThrow(() -> new EntityNotFoundException("Defect Status not found with name: "
                        + defectDto.getDefectStatus())));
        Defect mappedDefect = defectMapper.map(defectDto, defectToUpdate);

        Defect updatedDefect = defectRepository.save(mappedDefect);
        return defectMapper.mapToDto(updatedDefect);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefect(Integer id) {
        Defect defectToDelete = defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));

        Lot lot = defectToDelete.getLot();
        lot.removeDefect(defectToDelete);

        for (Action action : new ArrayList<>(defectToDelete.getActions())) {
            defectToDelete.deleteAction(action);
        }
        defectRepository.delete(defectToDelete);
    }
}
