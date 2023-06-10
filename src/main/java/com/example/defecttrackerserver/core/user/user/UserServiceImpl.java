package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserUtils.checkNullValues(userDto.getUsername(), userDto.getMail(),
                userDto.getPassword(), userDto.getRoles(), userDto.getLocation());

        if(userRepository.findByUsername(userDto.getUsername()).isPresent())
            throw new UserExistsException("Username already exists: " + userDto.getUsername());

        if(userRepository.findByMail(userDto.getMail()).isPresent())
            throw new UserExistsException("Mail already exists: " + userDto.getMail());

        User newUser = modelMapper.map(userDto, User.class);

        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User userToUpdate = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userDto.getId()));

        UserUtils.checkNullValues(userDto.getUsername(), userDto.getMail(), userDto.getPassword(),
                userDto.getRoles(), userDto.getLocation());

        if(userDto.getAssignedActions() == null)
            throw new IllegalArgumentException("AssignedActions must not be null");

        if(userRepository.findByUsername(userDto.getUsername()).isPresent())
            throw new UserExistsException("Username already exists: " + userToUpdate.getUsername());

        if(userRepository.findByMail(userDto.getMail()).isPresent())
            throw new UserExistsException("Mail already exists: " + userToUpdate.getMail());

        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setMail(userDto.getMail());
        userToUpdate.setPassword(userDto.getPassword());
        userToUpdate.setRoles(userDto.getRoles().stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(()-> new EntityNotFoundException("Role not found with id: " + role.getId())))
                .collect(Collectors.toSet()));

        userToUpdate.setLocation(modelMapper.map(userDto.getLocation(), Location.class));

        userToUpdate.setAssignedActions(userDto.getAssignedActions().stream()
                .map(action -> actionRepository.findById(action.getId()).orElseThrow(
                        () -> new EntityNotFoundException("Action not found with id: " + action.getId())
                ))
                .collect(Collectors.toSet()));
        User updatedUser = userRepository.save(userToUpdate);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return modelMapper.map(user, UserDto.class);
    }
}
