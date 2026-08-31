package com.nohaila.taskflow_api.repository;

import com.nohaila.taskflow_api.entity.Project;
import com.nohaila.taskflow_api.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long ownerId);

    Page<Project> findByOwnerIdAndNameContainingIgnoreCaseAndStatus(
            Long ownerId, String name, ProjectStatus status, Pageable pageable);

    Page<Project> findByOwnerIdAndNameContainingIgnoreCase(
            Long ownerId, String name, Pageable pageable);
}