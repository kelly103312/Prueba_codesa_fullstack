package com.codesa.user.service.demo.controller;

import com.codesa.user.service.demo.dto.UserDto;
import com.codesa.user.service.demo.security.AuthenticatedUser;
import com.codesa.user.service.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        AuthenticatedUser authenticated = (AuthenticatedUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        UserDto user = userService.getUserById(UUID.fromString(authenticated.userId()));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
}
