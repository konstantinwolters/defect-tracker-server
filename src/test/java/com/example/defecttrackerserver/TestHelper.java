package com.example.defecttrackerserver;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryDto;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusDto;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeDto;
import com.example.defecttrackerserver.core.defect.process.ProcessDto;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotDto;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TestHelper {

    public RoleDto setUpRoleDto(String name){
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName(name);
        return roleDto;
    }

    public LocationDto setUpLocationDto(String name){
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1);
        locationDto.setName(name);
        return locationDto;
    }

    public UserDto setUpUserDto(String username, String mail, String role, String location){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername(username);
        userDto.setMail(mail);
        userDto.setRoles(new HashSet<>(Set.of(role)));
        userDto.setLocation(location);
        userDto.setIsActive(true);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setChangedAt(LocalDateTime.now());
        userDto.setCreatedBy(2);
        userDto.setChangedBy(2);
        userDto.setAssignedActions(new HashSet<>(Set.of(1,2,3)));
        userDto.setFirstName("testFirstName");
        userDto.setLastName("testLastName");
        userDto.setCustomId("testCustomId");
        userDto.setPassword("testPassword");
        return userDto;
    }

    public CausationCategoryDto setUpCausationCategoryDto(String name){
        CausationCategoryDto causationCategoryDto = new CausationCategoryDto();
        causationCategoryDto.setId(1);
        causationCategoryDto.setName(name);
        return causationCategoryDto;
    }

    public DefectCommentDto setUpDefectCommentDto (String content, UserDto createdBy){
        DefectCommentDto defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(1);
        defectCommentDto.setCreatedBy(createdBy);
        defectCommentDto.setCreatedAt(LocalDateTime.now());
        return defectCommentDto;
    }

    public DefectImageDto setUpDefectImageDto(){
        DefectImageDto defectImageDto = new DefectImageDto();
        defectImageDto.setId(1);
        defectImageDto.setPath("testPath");
        return defectImageDto;
    }

    public DefectStatusDto setUpDefectStatusDto(String name){
        DefectStatusDto defectStatusDto = new DefectStatusDto();
        defectStatusDto.setId(1);
        defectStatusDto.setName(name);
        return defectStatusDto;
    }

    public DefectTypeDto setUpDefectTypeDto(String name){
        DefectTypeDto defectTypeDto = new DefectTypeDto();
        defectTypeDto.setId(1);
        defectTypeDto.setName(name);
        return defectTypeDto;
    }

    public ProcessDto setUpProcessDto(String name){
        ProcessDto processDto = new ProcessDto();
        processDto.setId(1);
        processDto.setName(name);
        return processDto;
    }

    public MaterialDto setUpMaterialDto(String name, Set<UserDto> responsibleUsers){
        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(1);
        materialDto.setResponsibleUsers(responsibleUsers);
        materialDto.setCustomId("testCustomId");
        return materialDto;
    }

    public SupplierDto setUpSupplierDto(String name){
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(1);
        supplierDto.setName(name);
        supplierDto.setCustomId("testCustomId");
        return supplierDto;
    }

    public LotDto setUpLotDto(String lotNumber, MaterialDto materialDto, SupplierDto supplierDto){
        LotDto lotDto = new LotDto();
        lotDto.setId(1);
        lotDto.setLotNumber(lotNumber);
        lotDto.setMaterial(materialDto);
        lotDto.setSupplier(supplierDto);
        return lotDto;
    }

    public DefectDto setUpDefectDto(String description,
                                    String lotNumber,
                                    String defectType,
                                    String defectStatus,
                                    String causationCategory,
                                    String process,
                                    String location,
                                    UserDto user){
        DefectImageDto defectImageDto = setUpDefectImageDto();
        DefectCommentDto defectCommentDto = setUpDefectCommentDto("testDescription", user);

        DefectDto defectDto = new DefectDto();
        defectDto.setId(1);
        defectDto.setDescription(description);
        defectDto.setLot(lotNumber);
        defectDto.setDefectStatus(defectStatus);
        defectDto.setDefectType(defectType);
        defectDto.setProcess(process);
        defectDto.setCausationCategory(causationCategory);
        defectDto.setLocation(location);
        defectDto.setCreatedBy(user);
        defectDto.setChangedBy(user);
        defectDto.setCreatedAt(LocalDateTime.now());
        defectDto.setChangedAt(LocalDateTime.now());
        defectDto.setImages(new ArrayList<>(List.of(defectImageDto)));
        defectDto.setDefectComments(new HashSet<>(Set.of(defectCommentDto)));
        return defectDto;
    }

    public ActionDto setUpActionDto(String description, UserDto user){
        ActionDto actionDto = new ActionDto();
        actionDto.setId(1);
        actionDto.setIsCompleted(false);
        actionDto.setDescription(description);
        actionDto.setAssignedUsers(new HashSet<>(Set.of(user)));
        actionDto.setDefect(1);
        actionDto.setCreatedBy(user);
        actionDto.setChangedBy(user);
        actionDto.setCreatedAt(LocalDateTime.now());
        actionDto.setChangedAt(LocalDateTime.now());
        actionDto.setDueDate(LocalDate.now());
        return actionDto;
    }


    public Role setUpRole(String name){
        Role role = new Role();
        role.setId(1);
        role.setName(name);
        return role;
    }

    public Location setUpLocation(String name){
        Location location = new Location();
        location.setId(1);
        location.setName(name);
        return location;
    }

    public User setUpUser(String username, String mail, Role role, Location location){
        User user = new User();
        user.setId(1);
        user.setUsername(username);
        user.setMail(mail);
        user.setRoles(new HashSet<>(Set.of(role)));
        user.setLocation(location);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setChangedAt(LocalDateTime.now());
        user.setCreatedById(2);
        user.setChangedById(2);
        user.setAssignedActions(new HashSet<>(Set.of(new Action())));
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setCustomId("testCustomId");
        user.setPassword("testPassword");
        return user;
    }

    public CausationCategory setUpCausationCategory(String name){
        CausationCategory causationCategory = new CausationCategory();
        causationCategory.setId(1);
        causationCategory.setName(name);
        return causationCategory;
    }

    public DefectComment setUpDefectComment (String content, User createdBy){
        DefectComment defectComment = new DefectComment();
        defectComment.setId(1);
        defectComment.setCreatedBy(createdBy);
        defectComment.setCreatedAt(LocalDateTime.now());
        return defectComment;
    }

    public DefectImage setUpDefectImage(){
        DefectImage defectImage = new DefectImage();
        defectImage.setId(1);
        defectImage.setPath("testPath");
        return defectImage;
    }

    public DefectStatus setUpDefectStatus(String name){
        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setId(1);
        defectStatus.setName(name);
        return defectStatus;
    }

    public DefectType setUpDefectType(String name){
        DefectType defectTypeDto = new DefectType();
        defectTypeDto.setId(1);
        defectTypeDto.setName(name);
        return defectTypeDto;
    }

    public Process setUpProcess(String name){
        Process process = new Process();
        process.setId(1);
        process.setName(name);
        return process;
    }

    public Material setUpMaterial(String name, Set<User> responsibleUsers){
        Material material = new Material();
        material.setId(1);
        material.setResponsibleUsers(responsibleUsers);
        material.setCustomId("testCustomId");
        return material;
    }

    public Supplier setUpSupplier(String name){
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName(name);
        supplier.setCustomId("testCustomId");
        return supplier;
    }

    public Lot setUpLot(String lotNumber, Material material, Supplier supplier){
        Lot lotDto = new Lot();
        lotDto.setId(1);
        lotDto.setLotNumber(lotNumber);
        lotDto.setMaterial(material);
        lotDto.setSupplier(supplier);
        return lotDto;
    }

    public Defect setUpDefect(String description,
                              Lot lot,
                              DefectType defectType,
                              DefectStatus defectStatus,
                              CausationCategory causationCategory,
                              Process process,
                              Location location,
                              User user){
        DefectImage defectImage = setUpDefectImage();
        DefectComment defectComment = setUpDefectComment("testDescription", user);

        Defect defect = new Defect();
        defect.setId(1);
        defect.setDescription(description);
        defect.setDefectStatus(defectStatus);
        defect.setDefectType(defectType);
        defect.setProcess(process);
        defect.setCausationCategory(causationCategory);
        defect.setLot(lot);
        defect.setLocation(location);
        defect.setCreatedBy(user);
        defect.setChangedBy(user);
        defect.setCreatedAt(LocalDateTime.now());
        defect.setChangedAt(LocalDateTime.now());
        defect.setImages(new ArrayList<>(List.of(defectImage)));
        defect.setDefectComments(new HashSet<>(Set.of(defectComment)));
        return defect;
    }

    public Action setUpAction(String description, User user, Defect defect){
        Action action = new Action();
        action.setId(1);
        action.setIsCompleted(false);
        action.setDescription(description);
        action.setAssignedUsers(new HashSet<>(Set.of(user)));
        action.setDefect(defect);
        action.setCreatedBy(user);
        action.setChangedBy(user);
        action.setCreatedAt(LocalDateTime.now());
        action.setChangedAt(LocalDateTime.now());
        action.setDueDate(LocalDate.now());
        return action;
    }
}
