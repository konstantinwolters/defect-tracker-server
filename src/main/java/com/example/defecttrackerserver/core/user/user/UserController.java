package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Save User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping()
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
            return userService.saveUser(userDto);
    }

    @Operation(
            summary = "Get User by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
            }
    )
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Get all Users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users found"),
            }
    )
    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Update User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
            }
    )
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Integer id, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @Operation(
            summary = "Delete User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
