package com.porraglobal.auth.service;

import com.porraglobal.auth.dto.RegisterRequest;
import com.porraglobal.auth.dto.UserResponse;
import com.porraglobal.auth.mapper.UserMapper;
import com.porraglobal.common.exception.ConflictException;
import com.porraglobal.common.security.JwtTokenProvider;
import com.porraglobal.users.entity.Role;
import com.porraglobal.users.entity.User;
import com.porraglobal.users.repository.RoleRepository;
import com.porraglobal.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "jwtExpirationMs", 86400000L);
        registerRequest = new RegisterRequest("juan", "juan@test.com", "password123", "Juan");
    }

    @Test
    void register_shouldCreateUserAndReturnToken() {
        when(userRepository.existsByUsername("juan")).thenReturn(false);
        when(userRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(roleRepository.findByName(Role.USER))
                .thenReturn(Optional.of(Role.builder().id(1L).name(Role.USER).build()));
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtTokenProvider.generateToken(anyString(), anyList())).thenReturn("jwt-token");
        when(userMapper.toResponse(any(User.class))).thenReturn(
                new UserResponse(1L, "juan", "juan@test.com", "Juan", null, Set.of(Role.USER), Instant.now()));

        var response = authService.register(registerRequest);

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.user().username()).isEqualTo("juan");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowConflict_whenUsernameExists() {
        when(userRepository.existsByUsername("juan")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("nombre de usuario");
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowConflict_whenEmailExists() {
        when(userRepository.existsByUsername("juan")).thenReturn(false);
        when(userRepository.existsByEmail("juan@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("email");
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldUseUsernameAsDisplayName_whenDisplayNameIsNull() {
        var request = new RegisterRequest("ana", "ana@test.com", "password123", null);
        when(userRepository.existsByUsername("ana")).thenReturn(false);
        when(userRepository.existsByEmail("ana@test.com")).thenReturn(false);
        when(roleRepository.findByName(Role.USER))
                .thenReturn(Optional.of(Role.builder().id(1L).name(Role.USER).build()));
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtTokenProvider.generateToken(anyString(), anyList())).thenReturn("jwt-token");
        when(userMapper.toResponse(any(User.class))).thenReturn(
                new UserResponse(2L, "ana", "ana@test.com", "ana", null, Set.of(Role.USER), Instant.now()));

        authService.register(request);

        verify(userRepository).save(org.mockito.ArgumentMatchers.argThat(
                u -> "ana".equals(u.getDisplayName())));
    }
}
