package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.location.LocationMapper;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleMapper;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
    private final DefectRepository defectRepository;
    private final DefectCommentRepository defectCommentRepository;
    private final ActionRepository actionRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final LocationMapper locationMapper;
    private final RoleMapper roleMapper;
    private final Utils utils;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto saveUser(UserDto userDto) {
        userDto.setId(null);

        if(userDto.getPassword() == null)
            throw new IllegalArgumentException("Password must not be null.");

        User newUser = userMapper.mapToEntity(userDto, new User());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: ROLE_USER"));
        newUser.addRole(role);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setCreatedById(securityService.getUser().getId());

        User savedUser = userRepository.save(newUser);
        return userMapper.mapToDto(savedUser);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = findUserById(id);
        return userMapper.mapToDto(user);
    }

    @Override
    public PaginatedResponse<UserDto> getUsers(
            String searchTerm,
            Boolean isActive,
            String locationIds,
            String roleIds,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate changedAtStart,
            LocalDate changedAtEnd,
            String createdByIds,
            String changedByIds,
            Integer page,
            Integer size,
            String sort
    ){
        List<Integer> locationIdList = utils.convertStringToListOfInteger(locationIds);
        List<Integer> roleIdList = utils.convertStringToListOfInteger(roleIds);
        List<Integer> createdByIdList = utils.convertStringToListOfInteger(createdByIds);
        List<Integer> changedByIdList = utils.convertStringToListOfInteger(changedByIds);

        Sort sorting = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] split = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(split[1]);
            sorting = Sort.by(direction, split[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Specification<User> spec = userSpecification.createSpecification(
                searchTerm,
                isActive,
                locationIdList,
                roleIdList,
                createdAtStart,
                createdAtEnd,
                changedAtStart,
                changedAtEnd,
                createdByIdList,
                changedByIdList
        );

        Page<User> users = userRepository.findAll(spec, pageable);
        List<UserDto> userDtos = users.stream().map(userMapper::mapToDto).toList();

        List<User> filteredUsers = userRepository.findAll(spec);

        return new PaginatedResponse<>(
                userDtos,
                users.getTotalPages(),
                (int) users.getTotalElements(),
                users.getNumber(),
                getFilteredUsers(filteredUsers) // provide distinct filter values for User meeting the filter criteria
        );
    }

    //Returns distinct filter values for User meeting the filter criteria
    public UserFilterValues getFilteredUsers(List<User> users) {
        List<Integer> userIds = users.stream().map(User::getId).toList();

        UserFilterValues userFilterValues = new UserFilterValues();
        userFilterValues.setIsActive(userRepository.findDistinctIsActive(userIds));
        userFilterValues.setLocations(utils.mapToSet(
                userRepository.findDistinctLocation(userIds), locationMapper::mapToDto));

        userFilterValues.setRoles(utils.mapToSet(
                userRepository.findDistinctRoles(userIds), roleMapper::mapToDto));

        userFilterValues.setCreatedDates(utils.mapToSet(
                userRepository.findDistinctCreatedAt(userIds), LocalDateTime::toLocalDate));

        userFilterValues.setChangedDates(utils.mapToSet(
                userRepository.findDistinctChangedAt(userIds), LocalDateTime::toLocalDate));

        List<User> createdByUsers = userRepository.findByIds(userRepository.findDistinctCreatedBy(userIds));
        List<User> changedByUsers = userRepository.findByIds(userRepository.findDistinctChangedBy(userIds));

        userFilterValues.setCreatedByUsers(utils.mapToSet(
                createdByUsers, UserInfo::new));

        userFilterValues.setChangedByUsers(utils.mapToSet(
                changedByUsers, UserInfo::new));

        return userFilterValues;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = findUserById(userId);
        
        if(userDto.getPassword() == null) {
            userDto.setPassword(user.getPassword());
        }else{
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User userToUpdate = userMapper.mapToEntity(userDto, user);
        userToUpdate.setChangedAt(LocalDateTime.now());
        userToUpdate.setChangedById(securityService.getUser().getId());

        User updatedUser = userRepository.save(userToUpdate);
        return userMapper.mapToDto(updatedUser);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public boolean deleteUser(Integer id) {
        boolean isDeleted;

        User userToDelete = findUserById(id);

        if(!defectRepository.findByChangedById(id).isEmpty()
        || !defectRepository.findByCreatedById(id).isEmpty()
        || !defectCommentRepository.findByCreatedById(id).isEmpty()
        || !actionRepository.findByChangedById(id).isEmpty()
        || !actionRepository.findByCreatedById(id).isEmpty()
        || !actionRepository.findByAssignedUsersId(id).isEmpty()
        || !userRepository.findByChangedById(id).isEmpty()
        || !userRepository.findByCreatedById(id).isEmpty()) {
            deactivateUser(id);
            isDeleted = false;
        }else {
            userRepository.delete(userToDelete);
            isDeleted = true;
        }

        return isDeleted;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deactivateUser(Integer id) {
        User userToDeactivate = findUserById(id);

        userToDeactivate.setIsActive(false);;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return userMapper.mapToDto(user);
    }

    private User findUserById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
