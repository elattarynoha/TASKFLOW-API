package com.nohaila.taskflow_api.repository;

import com.nohaila.taskflow_api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}