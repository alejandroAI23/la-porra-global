package com.porraglobal.users.controller;

import com.porraglobal.auth.dto.UserResponse;
import com.porraglobal.users.dto.UpdateUserRequest;
import com.porraglobal.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Perfil del usuario")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Obtener mi perfil")
    public UserResponse me() {
        return userService.getCurrentUser();
    }

    @PatchMapping("/me")
    @Operation(summary = "Actualizar mi perfil")
    public UserResponse updateMe(@Valid @RequestBody UpdateUserRequest request) {
        return userService.updateCurrentUser(request);
    }
}
