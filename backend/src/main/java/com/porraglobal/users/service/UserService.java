package com.porraglobal.users.service;

import com.porraglobal.auth.dto.UserResponse;
import com.porraglobal.auth.mapper.UserMapper;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.users.dto.UpdateUserRequest;
import com.porraglobal.users.entity.User;
import com.porraglobal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        return userMapper.toResponse(currentUserProvider.require());
    }

    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        User user = currentUserProvider.require();
        if (StringUtils.hasText(request.displayName())) {
            user.setDisplayName(request.displayName());
        }
        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }
        return userMapper.toResponse(userRepository.save(user));
    }
}
