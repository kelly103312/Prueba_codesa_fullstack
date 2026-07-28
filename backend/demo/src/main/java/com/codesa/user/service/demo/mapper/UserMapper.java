package com.codesa.user.service.demo.mapper;

import com.codesa.user.service.demo.dto.UserDto;
import com.codesa.user.service.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "enumToString")
    UserDto toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToEnum")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(UserDto dto);

    @Named("enumToString")
    default String enumToString(UserEntity.Role role) {
        return role != null ? role.name() : null;
    }

    @Named("stringToEnum")
    default UserEntity.Role stringToEnum(String role) {
        return role != null ? UserEntity.Role.valueOf(role) : null;
    }
}
