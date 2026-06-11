package com.porraglobal.common.security;

import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.users.entity.User;
import com.porraglobal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Resuelve el usuario autenticado a partir del SecurityContext.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public User require() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResourceNotFoundException("No hay usuario autenticado");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", auth.getName()));
    }

    public Long requireId() {
        return require().getId();
    }
}
