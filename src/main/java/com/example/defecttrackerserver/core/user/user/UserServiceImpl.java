package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DefectRepository defectRepository;
    private final DefectCommentRepository defectCommentRepository;
    private final ActionRepository actionRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.mapToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if(!securityService.getUsername().equals(user.getUsername())
        && !securityService.hasRole("ROLE_ADMIN")){
            throw new AccessDeniedException("You are not authorized to update this user's data");
        }

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
    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if(!defectRepository.findByChangedById(id).isEmpty()
        || !defectRepository.findByCreatedById(id).isEmpty()
        ||!defectCommentRepository.findByCreatedById(id).isEmpty()
        || !actionRepository.findByChangedById(id).isEmpty()
        || !actionRepository.findByCreatedById(id).isEmpty()
        || !actionRepository.findByAssignedUsersId(id).isEmpty()
        || !userRepository.findByChangedById(id).isEmpty()
        || !userRepository.findByCreatedById(id).isEmpty()) {
            deactivateUser(id);
        }else {
            userRepository.delete(userToDelete);
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deactivateUser(Integer id) {
        User userToDeactivate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        userToDeactivate.setIsActive(false);;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return userMapper.mapToDto(user);
    }
}
