package com.codesa.user.service.demo.service;

import com.codesa.user.service.demo.dto.UserDto;
import com.codesa.user.service.demo.exception.UserNotFoundException;
import com.codesa.user.service.demo.mapper.UserMapper;
import com.codesa.user.service.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el ID proporcionado"));
    }

    public UserDto getAuthenticatedUser(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el email proporcionado"));
    }

}
