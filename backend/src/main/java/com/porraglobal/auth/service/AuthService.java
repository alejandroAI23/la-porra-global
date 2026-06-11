package com.porraglobal.auth.service;

import com.porraglobal.auth.dto.AuthResponse;
import com.porraglobal.auth.dto.LoginRequest;
import com.porraglobal.auth.dto.RegisterRequest;
import com.porraglobal.auth.mapper.UserMapper;
import com.porraglobal.common.exception.ConflictException;
import com.porraglobal.common.security.JwtTokenProvider;
import com.porraglobal.users.entity.Role;
import com.porraglobal.users.entity.User;
import com.porraglobal.users.repository.RoleRepository;
import com.porraglobal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("El email ya está registrado");
        }

        Role userRole = roleRepository.findByName(Role.USER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(Role.USER).build()));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .displayName(request.displayName() != null ? request.displayName() : request.username())
                .roles(new HashSet<>(Set.of(userRole)))
                .build();
        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername(), roleNames(user));
        return AuthResponse.bearer(token, jwtExpirationMs, userMapper.toResponse(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        User user = userRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtTokenProvider.generateToken(user.getUsername(), roleNames(user));
        return AuthResponse.bearer(token, jwtExpirationMs, userMapper.toResponse(user));
    }

    private List<String> roleNames(User user) {
        return user.getRoles().stream().map(Role::getName).toList();
    }
}
