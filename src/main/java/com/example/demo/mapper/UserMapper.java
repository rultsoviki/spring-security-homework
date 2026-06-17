package com.example.demo.mapper;

import com.example.demo.controllers.dto.UserRequest;
import com.example.demo.controllers.dto.UserResponse;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName",source = "name")
    User toEntity(UserRequest userRequest);


    @Mapping(target = "roles",source = "roles",qualifiedByName = "mapRolesToNames")
    UserResponse toResponse(User user);

    @Named("mapRolesToNames")
    default  Set<String> mapRolesToNames(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
