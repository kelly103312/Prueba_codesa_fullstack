package com.codesa.user.service.project.mapper;

import com.codesa.user.service.project.dto.CreateProjectRequest;
import com.codesa.user.service.project.dto.ProjectDto;
import com.codesa.user.service.project.dto.ProjectListResponseDto;
import com.codesa.user.service.project.dto.UpdateProjectRequest;
import com.codesa.user.service.project.entity.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDto toDto(ProjectEntity entity);

    List<ProjectListResponseDto> toListDto(List<ProjectEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "ownerName", ignore = true)
    ProjectDto toDtoFromCreate(CreateProjectRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "ownerName", ignore = true)
    ProjectDto toDtoFromUpdate(UpdateProjectRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", defaultValue = "ACTIVE")
    ProjectEntity toEntity(ProjectDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "ownerName", ignore = true)
    void updateEntity(ProjectDto dto, @MappingTarget ProjectEntity entity);
}
