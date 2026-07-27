package com.codesa.user.service.project.mapper;

import com.codesa.user.service.project.dto.CreateTaskRequest;
import com.codesa.user.service.project.dto.TaskDto;
import com.codesa.user.service.project.dto.TaskListResponseDto;
import com.codesa.user.service.project.dto.UpdateTaskRequest;
import com.codesa.user.service.project.entity.TaskEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    TaskDto toDto(TaskEntity entity);

    List<TaskListResponseDto> toListDto(List<TaskEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaskDto toDtoFromCreate(CreateTaskRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaskDto toDtoFromUpdate(UpdateTaskRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", defaultValue = "CREATED")
    TaskEntity toEntity(TaskDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", defaultValue = "CREATED")
    void updateEntity(TaskDto dto, @MappingTarget TaskEntity entity);
}
