package com.codesa.user.service.project.repository;

import com.codesa.user.service.project.entity.TaskEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndProjectId(String name, Long projectId);
    List<TaskEntity> findByAssignedId(UUID assignedId);

}
