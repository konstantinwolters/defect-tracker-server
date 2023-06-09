package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.userException.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto saveUser(UserDto userDto) {
        User newUser = new User();
        return customUserMapper(userDto, newUser);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User userToUpdate = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userDto.getId()));
        return customUserMapper(userDto, userToUpdate);
    }

    private UserDto customUserMapper(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setMail(userDto.getMail());
        user.setRoles(userDto.getRoles().stream()
                .map(role -> modelMapper.map(role, Role.class))
                .collect(Collectors.toSet()));

        if(userDto.getLocation() == null)
            throw new IllegalArgumentException("Location must not be null");
        user.setLocation(modelMapper.map(userDto.getLocation(), Location.class));
        user.setAssignedActions(userDto.getAssignedActions().stream()
                .map(action -> modelMapper.map(action, Action.class))
                .collect(Collectors.toSet()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
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
