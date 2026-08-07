package com.nohaila.taskflow_api.service;

import com.nohaila.taskflow_api.dto.ProjectRequest;
import com.nohaila.taskflow_api.dto.ProjectResponse;
import com.nohaila.taskflow_api.entity.Project;
import com.nohaila.taskflow_api.entity.User;
import com.nohaila.taskflow_api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuthUtil authUtil;

    public ProjectResponse create(ProjectRequest request) {
        User currentUser = authUtil.getCurrentUser();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(currentUser);

        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public List<ProjectResponse> getMyProjects() {
        User currentUser = authUtil.getCurrentUser();
        return projectRepository.findByOwnerId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse getById(Long id) {
        Project project = findOwnedProjectOrThrow(id);
        return toResponse(project);
    }

    public void delete(Long id) {
        Project project = findOwnedProjectOrThrow(id);
        projectRepository.delete(project);
    }

    private Project findOwnedProjectOrThrow(Long id) {
        User currentUser = authUtil.getCurrentUser();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have access to this project");
        }
        return project;
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getEmail()
        );
    }
}