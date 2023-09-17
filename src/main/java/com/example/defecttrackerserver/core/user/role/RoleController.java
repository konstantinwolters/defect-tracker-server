package com.example.defecttrackerserver.core.user.role;

import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing {@link Role}.
 * Provides endpoints for retrieving roles.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@Tag(name = "Roles")
public class RoleController {
    private final RoleService roleService;

    @Operation(
            summary = "Get Role by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @Operation(
            summary = "Get all Roles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
            }
    )
    @GetMapping()
    public List<RoleDto> getRoles() {
        return roleService.getRoles();
    }
}
