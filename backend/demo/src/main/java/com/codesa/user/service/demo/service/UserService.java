package com.codesa.user.service.demo.service;

import com.codesa.user.service.demo.dto.UserDto;
import com.codesa.user.service.demo.entity.UserEntity;
import com.codesa.user.service.demo.exception.ConflictException;
import com.codesa.user.service.demo.exception.UserNotFoundException;
import com.codesa.user.service.demo.mapper.UserMapper;
import com.codesa.user.service.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ConflictException("El email " + userDto.getEmail() + " ya está registrado");
        }

        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setId(UUID.randomUUID());
        userEntity.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        if (userEntity.getRole() == null) {
            userEntity.setRole(UserEntity.Role.USER);
        }
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

}
