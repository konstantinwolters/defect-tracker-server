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
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import com.example.defecttrackerserver.notification.NotifyUsers;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final DefectSpecification defectSpecification;
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
    @NotifyUsers
    public DefectDto saveDefect(DefectDto defectDto, MultipartFile[] images) {
        User createdBy = securityService.getUser();

        // First, save new Defect
        Defect defect = new Defect();
        defectDto.setId(null);
        defectDto.setCreatedAt(LocalDateTime.now());
        defectDto.setCreatedBy(userMapper.mapToDto(createdBy));
        defectDto.setLocation(createdBy.getLocation().getName());
        defectDto.setImages(new ArrayList<>());

        Defect newDefect = defectMapper.map(defectDto, defect);

        DefectStatus defectStatus = defectStatusRepository.findByName("New")
                .orElseThrow(()-> new EntityNotFoundException("DefectStatus not found with name: 'New'"));

        CausationCategory causationCategory = causationCategoryRepository.findByName("Undefined")
                .orElseThrow(()-> new EntityNotFoundException("CausationCategory not found with name: 'Undefined'"));

        newDefect.setDefectStatus(defectStatus);
        newDefect.setCausationCategory(causationCategory);

        Defect savedDefect = defectRepository.save(newDefect);

        //Save images to file system and associate with Defect:
        addImages(savedDefect, images);

        Defect updatedDefect = defectRepository.save(savedDefect);
        return defectMapper.mapToDto(updatedDefect);
    }

    @Override
    public DefectDto getDefectById(Integer id) {
        Defect defect = findDefectById(id);
        return defectMapper.mapToDto(defect);
    }

    @Override
   public PaginatedResponse<DefectDto> getDefects(
            String search,
            String lotIds,
            String materialIds,
            String supplierIds,
            String defectStatusIds,
            String causationCategoryIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            String locationIds,
            String processIds,
            String defectTypeIds,
            String createdByIds,
            String changedByIds,
            Integer page,
            Integer size,
            String sort
    ){

        List<Integer> lotIdList = utils.convertStringToListOfInteger(lotIds);
        List<Integer> materialIdList = utils.convertStringToListOfInteger(materialIds);
        List<Integer> supplierIdList = utils.convertStringToListOfInteger(supplierIds);
        List<Integer> defectStatusIdList = utils.convertStringToListOfInteger(defectStatusIds);
        List<Integer> causationCategoryIdList = utils.convertStringToListOfInteger(causationCategoryIds);
        List<Integer> locationIdList = utils.convertStringToListOfInteger(locationIds);
        List<Integer> processIdList = utils.convertStringToListOfInteger(processIds);
        List<Integer> defectTypeIdList = utils.convertStringToListOfInteger(defectTypeIds);
        List<Integer> createdByIdList = utils.convertStringToListOfInteger(createdByIds);
        List<Integer> changedByIdList = utils.convertStringToListOfInteger(changedByIds);

        Sort sorting = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] split = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(split[1]);
            sorting = Sort.by(direction, split[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Specification<Defect> spec = defectSpecification.createSpecification( search, lotIdList, materialIdList, supplierIdList,
                defectStatusIdList, causationCategoryIdList, createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                locationIdList, processIdList, defectTypeIdList, createdByIdList, changedByIdList);

        Page<Defect> defects = defectRepository.findAll(spec, pageable);
        List<DefectDto> defectDtos =  defects.stream().map(defectMapper::mapToDto).toList();

        List<Defect> filteredDefects = defectRepository.findAll(spec);

        return new PaginatedResponse<>(
                defectDtos,
                defects.getTotalPages(),
                (int) defects.getTotalElements(),
                defects.getNumber(),
                getDefectFilterValues(filteredDefects) // provide distinct filter values for Defects meeting the filter criteria
        );
    }


    //Returns distinct filter values for Defects meeting the filter criteria
    @Override
    public DefectFilterValues getDefectFilterValues(List<Defect> defects) {
        List<Integer> defectIds = defects.stream().map(Defect::getId).toList();
        DefectFilterValues defectFilterValues = new DefectFilterValues();

        defectFilterValues.setLots(utils.mapToSet(
                defectRepository.findDistinctLots(defectIds), LotInfo::new));

        defectFilterValues.setMaterials(utils.mapToSet(
                defectRepository.findDistinctMaterials(defectIds), materialMapper::mapToDto));

        defectFilterValues.setSuppliers(utils.mapToSet(
                defectRepository.findDistinctSuppliers(defectIds), supplierMapper::mapToDto));

        defectFilterValues.setLocations(utils.mapToSet(
                defectRepository.findDistinctLocations(defectIds), locationMapper::mapToDto));

        defectFilterValues.setProcesses(utils.mapToSet(
                defectRepository.findDistinctProcesses(defectIds), processMapper::mapToDto));

        defectFilterValues.setCreatedByUsers(utils.mapToSet(
                defectRepository.findDistinctCreatedBy(defectIds), UserInfo::new));

        defectFilterValues.setChangedByUsers(utils.mapToSet(
                defectRepository.findDistinctChangedBy(defectIds), UserInfo::new));

        defectFilterValues.setDefectStatuses(utils.mapToSet(
                defectRepository.findDistinctDefectStatuses(defectIds), defectStatusMapper::mapToDto));

        defectFilterValues.setCausationCategories(utils.mapToSet(
                defectRepository.findDistinctCausationCategories(defectIds), causationCategoryMapper::mapToDto));

        defectFilterValues.setDefectTypes(utils.mapToSet(
                defectRepository.findDistinctDefectTypes(defectIds), defectTypeMapper::mapToDto));

        defectFilterValues.setCreatedDates(utils.mapToSet(
                defectRepository.findDistinctCreatedAt(defectIds), LocalDateTime::toLocalDate));

        Set<LocalDateTime> changedDates = defectRepository.findDistinctChangedAt(defectIds);
        defectFilterValues.setChangedDates(
                changedDates == null ? null : utils.mapToSet(changedDates, LocalDateTime::toLocalDate)
        );

        return defectFilterValues;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public DefectDto updateDefect(Integer defectId, DefectDto defectDto, MultipartFile[] images) {
        Defect defectToUpdate = findDefectById(defectId);

        //Check if images have been removed:
        List<Integer> dtoImageIds = defectDto.getImages().stream()
                .map(DefectImageDto::getId)
                .toList();

        List<DefectImage> imagesToRemove = defectToUpdate.getImages().stream()
                .filter(image -> !dtoImageIds.contains(image.getId()))
                .toList();

        //If yes, remove from filesystem:
        for(DefectImage image : imagesToRemove){
            utils.removeFileFromFileSystem(image.getPath());
        }

        //Map and update Defect
        Defect updatedDefect = defectMapper.map(defectDto, defectToUpdate);

        updatedDefect.setChangedBy(securityService.getUser());
        updatedDefect.setChangedAt(LocalDateTime.now());

        // If images have been added, save to filesystem and associate with Defect:
        if(images != null){
            addImages(updatedDefect, images);
        }

        Defect savedDefect = defectRepository.save(updatedDefect);
        return defectMapper.mapToDto(savedDefect);
    }

    private void addImages(Defect defect, MultipartFile[] images) {
        // 1. Create a folder with the defect's ID as name
        String folderPath = imageFolderPath + File.separator + defect.getId();
        utils.createDirectory(folderPath);

        // 2. Save images to filesystem and associate with Defect
        for (MultipartFile image : images) {
            utils.validateImage(image);
            String path = utils.saveImageToFileSystem(image, folderPath);
            DefectImage defectImage = new DefectImage();
            defectImage.setPath(path);
            defect.addDefectImage(defectImage);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefect(Integer id) {
        Defect defectToDelete = findDefectById(id);

        Lot lot = defectToDelete.getLot();
        lot.removeDefect(defectToDelete);

        for (Action action : new ArrayList<>(defectToDelete.getActions())) {
            defectToDelete.deleteAction(action);
        }
        defectRepository.delete(defectToDelete);
    }

    Defect findDefectById(Integer id){
        return defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));
    }
}
