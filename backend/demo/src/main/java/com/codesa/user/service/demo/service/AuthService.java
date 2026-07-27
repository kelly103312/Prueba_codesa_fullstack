package com.codesa.user.service.demo.service;

import com.codesa.user.service.demo.dto.AuthResponseDto;
import com.codesa.user.service.demo.dto.AuthenticatedRequestDto;
import com.codesa.user.service.demo.entity.UserEntity;
import com.codesa.user.service.demo.exception.InvalidCredentialsException;
import com.codesa.user.service.demo.exception.UserNotFoundException;
import com.codesa.user.service.demo.mapper.UserMapper;
import com.codesa.user.service.demo.repository.UserRepository;
import com.codesa.user.service.demo.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserMapper userMapper, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDto login(AuthenticatedRequestDto request) {
        log.info("=== LOGIN ===");
        log.info("Email: [{}]", request.getEmail());
        log.info("SHA-256 recibido (frontend): [{}]", request.getPassword());

        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            log.info("Usuario NO encontrado con email: [{}]", request.getEmail());
            throw new UserNotFoundException("Email no encontrado");
        }

        UserEntity entity = user.get();
        log.info("Usuario BD: email=[{}], hash=[{}]", entity.getEmail(), entity.getPasswordHash());

        String shaFromFront = request.getPassword();
        boolean match = passwordEncoder.matches(shaFromFront, entity.getPasswordHash());
        log.info("BCrypt.matches(sha256, hashBD) = {}", match);

        if (match) {
            String token = jwtService.generateToken(entity.getId().toString(), entity.getFullName(), entity.getRole().name());
            return new AuthResponseDto(userMapper.toDto(entity), token);
        }

        throw new InvalidCredentialsException("Credenciales inválidas");
    }
}
