package com.codesa.user.service.project.repository;

import com.codesa.user.service.project.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    boolean existsByProjectId(Long projectId);

    List<TaskEntity> findByProjectId(Long projectId);

    List<TaskEntity> findByAssignedId(UUID assignedId);
}
