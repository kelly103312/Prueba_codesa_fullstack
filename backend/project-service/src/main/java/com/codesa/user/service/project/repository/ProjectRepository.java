package com.codesa.user.service.project.repository;

import com.codesa.user.service.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    boolean existsByName(String name);

    List<ProjectEntity> findByAssignedId(UUID assignedId);

}
