package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.user.dto.UserCreationDto;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import com.example.defecttrackerserver.core.user.user.dto.UserUpdateDto;
import com.example.defecttrackerserver.core.user.user.userException.UserExistsException;
import com.example.defecttrackerserver.core.user.user.userException.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto saveUser(UserCreationDto userCreationDto) {
        User newUser = new User();
        UserUtils.checkNullValues(userCreationDto.getUsername(), userCreationDto.getMail(),
                userCreationDto.getPassword(), userCreationDto.getRoles(), userCreationDto.getLocation());

        if(userRepository.findByUsername(userCreationDto.getUsername()).isPresent())
            throw new UserExistsException("Username already exists: " + userCreationDto.getUsername());

        if(userRepository.findByMail(userCreationDto.getMail()).isPresent())
            throw new UserExistsException("Mail already exists: " + userCreationDto.getMail());

        newUser.setUsername(userCreationDto.getUsername());
        newUser.setFirstName(userCreationDto.getFirstName());
        newUser.setLastName(userCreationDto.getLastName());
        newUser.setMail(userCreationDto.getMail());
        newUser.setPassword(userCreationDto.getPassword());
        newUser.setRoles(userCreationDto.getRoles().stream()
                .map(role -> modelMapper.map(role, Role.class))
                .collect(Collectors.toSet()));

        newUser.setLocation(modelMapper.map(userCreationDto.getLocation(), Location.class));

        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User userToUpdate = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userUpdateDto.getId()));

        UserUtils.checkNullValues(userUpdateDto.getUsername(), userUpdateDto.getMail(), userUpdateDto.getPassword(),
                userUpdateDto.getRoles(), userUpdateDto.getLocation());

        if(userUpdateDto.getAssignedActions() == null)
            throw new IllegalArgumentException("AssignedActions must not be null or empty");

        if(userRepository.findByUsername(userUpdateDto.getUsername()).isPresent())
            throw new UserExistsException("Username already exists: " + userToUpdate.getUsername());

        if(userRepository.findByMail(userUpdateDto.getMail()).isPresent())
            throw new UserExistsException("Mail already exists: " + userToUpdate.getMail());

        userToUpdate.setUsername(userUpdateDto.getUsername());
        userToUpdate.setFirstName(userUpdateDto.getFirstName());
        userToUpdate.setLastName(userUpdateDto.getLastName());
        userToUpdate.setMail(userUpdateDto.getMail());
        userToUpdate.setPassword(userUpdateDto.getPassword());
        userToUpdate.setRoles(userUpdateDto.getRoles().stream()
                .map(role -> modelMapper.map(role, Role.class))
                .collect(Collectors.toSet()));

        userToUpdate.setLocation(modelMapper.map(userUpdateDto.getLocation(), Location.class));

        userToUpdate.setAssignedActions(userUpdateDto.getAssignedActions().stream()
                .map(action -> modelMapper.map(action, Action.class))
                .collect(Collectors.toSet()));
        User updatedUser = userRepository.save(userToUpdate);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return modelMapper.map(user, UserDto.class);
    }
}
