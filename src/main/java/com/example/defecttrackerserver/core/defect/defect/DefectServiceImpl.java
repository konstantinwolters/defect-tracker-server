package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryMapper;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusMapper;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeMapper;
import com.example.defecttrackerserver.core.defect.process.ProcessMapper;
import com.example.defecttrackerserver.core.location.LocationMapper;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotInfo;
import com.example.defecttrackerserver.core.lot.material.MaterialMapper;
import com.example.defecttrackerserver.core.lot.supplier.SupplierMapper;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final CausationCategoryRepository causationCategoryRepository;
    private final DefectMapper defectMapper;
    private final SecurityService securityService;
    private final MaterialMapper materialMapper;
    private final SupplierMapper supplierMapper;
    private final LocationMapper locationMapper;
    private final ProcessMapper processMapper;
    private final CausationCategoryMapper causationCategoryMapper;
    private final UserMapper userMapper;
    private final DefectTypeMapper defectTypeMapper;
    private final DefectStatusMapper defectStatusMapper;
    private final Utils utils;

    @Value("${IMAGE.UPLOAD-PATH}")
    String imageFolderPath;

    @Override
    @Transactional
    public DefectDto saveDefect(DefectDto defectDto, MultipartFile[] images) {

        // First, save new Defect
        Defect defect = new Defect();
        defectDto.setId(null);
        defectDto.setCreatedAt(LocalDateTime.now());
        defectDto.setCreatedBy(userMapper.mapToDto(securityService.getUser()));
        defectDto.setImages(new ArrayList<>());

        Defect newDefect = defectMapper.map(defectDto, defect);

        DefectStatus defectStatus = defectStatusRepository.findByName("New")
                .orElseThrow(()-> new EntityNotFoundException("DefectStatus not found with name: 'New'"));
        newDefect.setDefectStatus(defectStatus);

        CausationCategory causationCategory = causationCategoryRepository.findByName("Undefined")
                .orElseThrow(()-> new EntityNotFoundException("CausationCategory not found with name: 'Undefined'"));
        newDefect.setCausationCategory(causationCategory);

        Defect savedDefect = defectRepository.save(newDefect);

        // Then add images to saved Defect
        // 1. Create a folder with the defect's ID as name
        String folderPath = imageFolderPath + File.separator + savedDefect.getId();
        utils.createDirectory(folderPath);

        // 2. Save images to filesystem and add paths as DefectImages to Defect
        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            utils.validateImage(image);
            String path = utils.saveImageToFileSystem(image, folderPath, savedDefect.getId(), i + 1);
            DefectImage defectImage = new DefectImage();
            defectImage.setPath(path);
            savedDefect.addDefectImage(defectImage);
        }

        Defect updatedDefect = defectRepository.save(savedDefect);

        return defectMapper.mapToDto(updatedDefect);
    }

    @Override
    public DefectDto getDefectById(Integer id) {
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));
        return defectMapper.mapToDto(defect);
    }

    @Override
   public PaginatedResponse<DefectDto> getDefects(
            String searchTerm,
            List<Integer> lotIds,
            List<Integer> materials,
            List<Integer> suppliers,
            List<Integer> defectStatusIds,
            List<Integer> causationCategoriesIds,
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
    ){
        Specification<Defect> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("description")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("id").as(String.class)), "%" + searchTerm.toLowerCase() + "%")
                    )
            );
        }

        if(lotIds != null && !lotIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("lot").get("id").in(lotIds));
        }

        if(materials != null && !materials.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("lot").get("material").get("id").in(materials));
        }

        if(suppliers != null && !suppliers.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("lot").get("suppliers").get("id").in(suppliers));
        }

        if(defectStatusIds != null && !defectStatusIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("defectStatus").get("id").in(defectStatusIds));
        }

        if(causationCategoriesIds != null && !causationCategoriesIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("causationCategory").get("id").in(causationCategoriesIds));
        }

        if (createdAtStart != null && createdAtEnd != null) {
            LocalDateTime startOfDay = createdAtStart.atStartOfDay();
            LocalDateTime endOfDay = createdAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startOfDay, endOfDay));
        }

        if (changedAtStart != null && changedAtEnd != null) {
            LocalDateTime startOfDay = changedAtStart.atStartOfDay();
            LocalDateTime endOfDay = changedAtEnd.atStartOfDay().plusDays(1).minusSeconds(1);

            spec = spec.and((root, query, cb) -> cb.between(root.get("changedAt"), startOfDay, endOfDay));
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

        if(changedByIds != null && !changedByIds.isEmpty()){
            spec = spec.and((root, query, cb) -> root.get("changedBy").get("id").in(changedByIds));
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
        defectFilterValues.setLots(
                defectRepository.findDistinctLots(defectIds).stream()
                        .map(LotInfo::new)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setMaterials(
                defectRepository.findDistinctMaterials(defectIds).stream()
                        .map(materialMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setSuppliers(
                defectRepository.findDistinctSuppliers(defectIds).stream()
                        .map(supplierMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setLocations(
                defectRepository.findDistinctLocations(defectIds).stream()
                        .map(locationMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setProcesses(
                defectRepository.findDistinctProcesses(defectIds).stream()
                        .map(processMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setCreatedByUsers(
                defectRepository.findDistinctCreatedBy(defectIds).stream()
                        .map(UserInfo::new)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setChangedByUsers(
                defectRepository.findDistinctChangedBy(defectIds).stream()
                        .map(UserInfo::new)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setDefectStatuses(
                defectRepository.findDistinctDefectStatuses(defectIds).stream()
                        .map(defectStatusMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setCausationCategories(
                defectRepository.findDistinctCausationCategories(defectIds).stream()
                        .map(causationCategoryMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setDefectTypes(
                defectRepository.findDistinctDefectTypes(defectIds).stream()
                        .map(defectTypeMapper::mapToDto)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setCreatedDates(
                defectRepository.findDistinctCreatedAt(defectIds).stream()
                        .map(LocalDateTime::toLocalDate)
                        .collect(Collectors.toSet())
        );
        defectFilterValues.setChangedDates(
                defectRepository.findDistinctChangedAt(defectIds).stream()
                        .map(LocalDateTime::toLocalDate)
                        .collect(Collectors.toSet())
        );
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
        defectToUpdate.setChangedBy(securityService.getUser());
        defectToUpdate.setChangedAt(LocalDateTime.now());
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
