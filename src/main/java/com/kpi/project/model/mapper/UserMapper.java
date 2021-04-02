package com.kpi.project.model.mapper;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToUser(UserDto userDto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "roles")
    UserDto userToDto(User user);

    @Named("roles")
    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());
    }
}
