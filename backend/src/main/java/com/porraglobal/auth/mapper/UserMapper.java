package com.porraglobal.auth.mapper;

import com.porraglobal.auth.dto.UserResponse;
import com.porraglobal.users.entity.Role;
import com.porraglobal.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToNames")
    UserResponse toResponse(User user);

    @org.mapstruct.Named("rolesToNames")
    default Set<String> rolesToNames(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
