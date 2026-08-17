package com.nohaila.taskflow_api.repository;

import com.nohaila.taskflow_api.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
}