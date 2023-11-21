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
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessDto;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestHelper {

    public static RoleDto setUpRoleDto(){
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("testRole");
        return roleDto;
    }

    public static LocationDto setUpLocationDto(){
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1);
        locationDto.setName("testLocation");
        return locationDto;
    }

    public static UserDto setUpUserDto(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testUsername");
        userDto.setMail("testMail");
        userDto.setRoles(new HashSet<>(Set.of("testRole")));
        userDto.setLocation("testLocation");
        userDto.setIsActive(true);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setChangedAt(LocalDateTime.now());
        userDto.setCreatedBy(2);
        userDto.setChangedBy(2);
        userDto.setAssignedActions(new HashSet<>(Set.of(1)));
        userDto.setFirstName("testFirstName");
        userDto.setLastName("testLastName");
        userDto.setCustomId("testCustomId");
        userDto.setPassword("testPassword");
        return userDto;
    }

    public static CausationCategoryDto setUpCausationCategoryDto(){
        CausationCategoryDto causationCategoryDto = new CausationCategoryDto();
        causationCategoryDto.setId(1);
        causationCategoryDto.setName("testCausationCategory");
        return causationCategoryDto;
    }

    public static DefectCommentDto setUpDefectCommentDto (){
        DefectCommentDto defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(1);
        defectCommentDto.setContent("testContent");
        defectCommentDto.setCreatedBy(new UserDto());
        defectCommentDto.setCreatedAt(LocalDateTime.now());
        return defectCommentDto;
    }

    public static DefectImageDto setUpDefectImageDto(){
        DefectImageDto defectImageDto = new DefectImageDto();
        defectImageDto.setId(1);
        defectImageDto.setPath("testPath");
        return defectImageDto;
    }

    public static DefectStatusDto setUpDefectStatusDto(){
        DefectStatusDto defectStatusDto = new DefectStatusDto();
        defectStatusDto.setId(1);
        defectStatusDto.setName("testDefectStatus");
        return defectStatusDto;
    }

    public static DefectTypeDto setUpDefectTypeDto(){
        DefectTypeDto defectTypeDto = new DefectTypeDto();
        defectTypeDto.setId(1);
        defectTypeDto.setName("testDefectType");
        return defectTypeDto;
    }

    public static ProcessDto setUpProcessDto(){
        ProcessDto processDto = new ProcessDto();
        processDto.setId(1);
        processDto.setName("testProcess");
        return processDto;
    }

    public static MaterialDto setUpMaterialDto(){
        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(1);
        materialDto.setName("testMaterial");
        materialDto.setResponsibleUsers(new HashSet<>(Set.of(new UserDto())));
        materialDto.setCustomId("testCustomId");
        return materialDto;
    }

    public static SupplierDto setUpSupplierDto(){
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(1);
        supplierDto.setName("testSupplier");
        supplierDto.setCustomId("testCustomId");
        return supplierDto;
    }

    public static LotDto setUpLotDto(){
        LotDto lotDto = new LotDto();
        lotDto.setId(1);
        lotDto.setLotNumber("testNumber");
        lotDto.setMaterial(new MaterialDto());
        lotDto.setSupplier(new SupplierDto());
        lotDto.setDefects(new HashSet<>(Set.of(1)));
        return lotDto;
    }

    public static DefectDto setUpDefectDto(){
        DefectDto defectDto = new DefectDto();
        defectDto.setId(1);
        defectDto.setDescription("testDescription");
        defectDto.setLot(new LotDto());
        defectDto.setDefectStatus("testDefectStatus");
        defectDto.setDefectType("testDefectType");
        defectDto.setProcess("testProcess");
        defectDto.setCausationCategory("testCausationCategory");
        defectDto.setLocation("testLocation");
        defectDto.setCreatedBy(new UserDto());
        defectDto.setChangedBy(new UserDto());
        defectDto.setCreatedAt(LocalDateTime.now());
        defectDto.setChangedAt(LocalDateTime.now());
        defectDto.setActions(new HashSet<>(Set.of(new ActionDto())));
        defectDto.setImages(new ArrayList<>(List.of(new DefectImageDto())));
        defectDto.setDefectComments(new HashSet<>(Set.of(new DefectCommentDto())));
        return defectDto;
    }

    public static ActionDto setUpActionDto(){
        ActionDto actionDto = new ActionDto();
        actionDto.setId(1);
        actionDto.setIsCompleted(false);
        actionDto.setDescription("testDescription");
        actionDto.setAssignedUsers(new HashSet<>(Set.of(new UserDto())));
        actionDto.setDefect(1);
        actionDto.setCreatedBy(new UserDto());
        actionDto.setChangedBy(new UserDto());
        actionDto.setCreatedAt(LocalDateTime.now());
        actionDto.setChangedAt(LocalDateTime.now());
        actionDto.setDueDate(LocalDate.now());
        return actionDto;
    }


    public static Role setUpRole(){
        Role role = new Role();
        role.setId(1);
        role.setName("testRole");
        return role;
    }

    public static Location setUpLocation(){
        Location location = new Location();
        location.setId(1);
        location.setName("testLocation");
        return location;
    }

    public static User setUpUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("testUsername");
        user.setMail("testMail");
        user.setRoles(new HashSet<>(Set.of(new Role())));
        user.setLocation(new Location());
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

    public static CausationCategory setUpCausationCategory(){
        CausationCategory causationCategory = new CausationCategory();
        causationCategory.setId(1);
        causationCategory.setName("testCausationCategory");
        return causationCategory;
    }

    public static DefectComment setUpDefectComment (){
        DefectComment defectComment = new DefectComment();
        defectComment.setId(1);
        defectComment.setContent("testContent");
        defectComment.setCreatedBy(new User());
        defectComment.setCreatedAt(LocalDateTime.now());
        return defectComment;
    }

    public static DefectImage setUpDefectImage(){
        DefectImage defectImage = new DefectImage();
        defectImage.setId(1);
        defectImage.setPath("testPath");
        return defectImage;
    }

    public static DefectStatus setUpDefectStatus(){
        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setId(1);
        defectStatus.setName("testDefectStatus");
        return defectStatus;
    }

    public static DefectType setUpDefectType(){
        DefectType defectTypeDto = new DefectType();
        defectTypeDto.setId(1);
        defectTypeDto.setName("testDefectType");
        return defectTypeDto;
    }

    public static Process setUpProcess(){
        Process process = new Process();
        process.setId(1);
        process.setName("testProcess");
        return process;
    }

    public static Material setUpMaterial(){
        Material material = new Material();
        material.setId(1);
        material.setName("testMaterial");
        material.setResponsibleUsers(new HashSet<>(Set.of(new User())));
        material.setCustomId("testCustomId");
        return material;
    }

    public static Supplier setUpSupplier(){
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("testSupplier");
        supplier.setCustomId("testCustomId");
        return supplier;
    }

    public static Lot setUpLot(){
        Lot lot = new Lot();
        lot.setId(1);
        lot.setLotNumber("testNumber");
        lot.setMaterial(new Material());
        lot.setSupplier(new Supplier());
        lot.setDefects(new HashSet<>(Set.of(new Defect())));
        return lot;
    }

    public static Defect setUpDefect(){
        Defect defect = new Defect();
        defect.setId(1);
        defect.setDescription("testDescription");
        defect.setDefectStatus(new DefectStatus());
        defect.setDefectType(new DefectType());
        defect.setProcess(new Process());
        defect.setCausationCategory(new CausationCategory());
        defect.setLot(new Lot());
        defect.setLocation(new Location());
        defect.setCreatedBy(new User());
        defect.setChangedBy(new User());
        defect.setCreatedAt(LocalDateTime.now());
        defect.setChangedAt(LocalDateTime.now());
        defect.setActions(new HashSet<>(Set.of(new Action())));
        defect.setImages(new ArrayList<>(List.of(new DefectImage())));
        defect.setDefectComments(new HashSet<>(Set.of(new DefectComment())));
        return defect;
    }

    public static Action setUpAction(){
        Action action = new Action();
        action.setId(1);
        action.setIsCompleted(false);
        action.setDescription("testDescription");
        action.setAssignedUsers(new HashSet<>(Set.of(new User())));
        action.setDefect(new Defect());
        action.setCreatedBy(new User());
        action.setChangedBy(new User());
        action.setCreatedAt(LocalDateTime.now());
        action.setChangedAt(LocalDateTime.now());
        action.setDueDate(LocalDate.now());
        return action;
    }
}
