package com.codesa.user.service.demo.controller;

import com.codesa.user.service.demo.dto.ApiResponse;
import com.codesa.user.service.demo.dto.UserDto;
import com.codesa.user.service.demo.security.AuthenticatedUser;
import com.codesa.user.service.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario encontrado", user));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getAuthenticatedUser() {
        AuthenticatedUser authenticated = (AuthenticatedUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        UserDto user = userService.getUserById(UUID.fromString(authenticated.userId()));
        return ResponseEntity.ok(ApiResponse.ok("Usuario autenticado", user));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Lista de usuarios", users));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Usuario creado exitosamente", createdUser));
    }
    
}
