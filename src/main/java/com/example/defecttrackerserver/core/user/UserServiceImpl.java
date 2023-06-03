package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        //TODO: Assign a basic role to every new user
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUser(Integer id) {
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
    public UserDto updateUser(Integer id, UserDto userDto) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setPassword(userDto.getPassword());
        userToUpdate.setMail(userDto.getMail());
        //userToUpdate.setRoles(userDto.getRoles());
        userToUpdate.setLocation(userDto.getLocation());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
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
    public UserDto findByUsername(String username) {
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
