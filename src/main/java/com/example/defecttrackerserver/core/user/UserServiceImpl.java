package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    private final ActionRepository actionRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto saveUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setPassword(userDto.getPassword());
        newUser.setMail(userDto.getMail());
        newUser.setRoles(userDto.getRoles());
        newUser.setLocation(userDto.getLocation());
        newUser.setAssignedActions(userDto.getAssignedActions());


        //TODO: Assign a basic role to every new user
        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
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
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userDto.getId()));
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setPassword(userDto.getPassword());
        userToUpdate.setMail(userDto.getMail());
        userToUpdate.setRoles(userDto.getRoles());
        userToUpdate.setLocation(userDto.getLocation());
        userToUpdate.setAssignedActions(userDto.getAssignedActions());
        User savedUser = userRepository.save(userToUpdate);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Set<Role> getRoles(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return user.getRoles();
    }
}
