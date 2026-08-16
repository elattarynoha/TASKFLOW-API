package com.nohaila.taskflow_api.controller;

import com.nohaila.taskflow_api.dto.ProjectRequest;
import com.nohaila.taskflow_api.dto.ProjectResponse;
import com.nohaila.taskflow_api.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse create(@RequestBody ProjectRequest request) {
        return projectService.create(request);
    }

    @GetMapping
    public List<ProjectResponse> getMyProjects() {
        return projectService.getMyProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @PathVariable Long id) {
        projectService.delete(id);
    }
}