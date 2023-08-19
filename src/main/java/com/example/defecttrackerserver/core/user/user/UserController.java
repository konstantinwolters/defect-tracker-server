package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            summary = "Get all Users with search, filter and sort values",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users found"),
            }
    )
    @GetMapping()
    PaginatedResponse<UserDto> getUsers(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String locationIds,
            @RequestParam(required = false) String roleIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate changedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate changedAtEnd,
            @RequestParam(required = false) String createdByIds,
            @RequestParam(required = false) String changedByIds,
            @RequestParam(required = false, defaultValue = "0" )Integer page,
            @RequestParam(required = false, defaultValue = "10")Integer size,
            @RequestParam(required = false) String sort
    ){
        return userService.getUsers(searchTerm, isActive, locationIds, roleIds, createdAtStart,
                createdAtEnd, changedAtStart, changedAtEnd, createdByIds, changedByIds, page,
                size, sort);
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
