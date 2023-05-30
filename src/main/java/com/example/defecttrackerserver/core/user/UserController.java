package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public UserDto saveUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping("/users/search")
    public UserDto findByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/users/{id}/roles")
    public Set<Role> getRoles(@PathVariable Integer id) {
        return userService.getRoles(id);
    }

}
