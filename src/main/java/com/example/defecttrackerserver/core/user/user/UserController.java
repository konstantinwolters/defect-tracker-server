package com.example.defecttrackerserver.core.user.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
            return userService.saveUser(userDto);
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping()
    public UserDto updateUser(@Valid@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
