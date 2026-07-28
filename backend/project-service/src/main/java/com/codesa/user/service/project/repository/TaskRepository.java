package com.codesa.user.service.project.repository;

import com.codesa.user.service.project.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    boolean existsByProjectId(Long projectId);

    List<TaskEntity> findByProjectId(Long projectId);

    List<TaskEntity> findByAssignedId(UUID assignedId);

    @Query("SELECT t FROM TaskEntity t WHERE t.status NOT IN ('DONE', 'OVERDUE') AND t.dueDate < :now")
    List<TaskEntity> findOverdueTasks(LocalDateTime now);
}
