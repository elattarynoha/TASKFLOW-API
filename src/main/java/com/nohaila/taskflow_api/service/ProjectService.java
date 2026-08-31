package com.nohaila.taskflow_api.service;

import com.nohaila.taskflow_api.dto.PagedResponse;
import com.nohaila.taskflow_api.dto.ProjectRequest;
import com.nohaila.taskflow_api.dto.ProjectResponse;
import com.nohaila.taskflow_api.entity.Project;
import com.nohaila.taskflow_api.entity.ProjectStatus;
import com.nohaila.taskflow_api.entity.User;
import com.nohaila.taskflow_api.exception.AccessDeniedException;
import com.nohaila.taskflow_api.exception.ResourceNotFoundException;
import com.nohaila.taskflow_api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuthUtil authUtil;

    private static final long DEADLINE_SOON_DAYS = 3;

    public ProjectResponse create(ProjectRequest request) {
        User currentUser = authUtil.getCurrentUser();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() != null ? request.getStatus() : ProjectStatus.ACTIVE);
        project.setDueDate(request.getDueDate());
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

    public PagedResponse<ProjectResponse> getMyProjectsPaged(
            int page, int size, String sortBy, String direction, String search, ProjectStatus status) {

        User currentUser = authUtil.getCurrentUser();

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        String searchTerm = search != null ? search : "";

        Page<Project> projectPage = (status != null)
                ? projectRepository.findByOwnerIdAndNameContainingIgnoreCaseAndStatus(
                        currentUser.getId(), searchTerm, status, pageable)
                : projectRepository.findByOwnerIdAndNameContainingIgnoreCase(
                        currentUser.getId(), searchTerm, pageable);

        Page<ProjectResponse> responsePage = projectPage.map(this::toResponse);
        return PagedResponse.of(responsePage);
    }

    public ProjectResponse getById(Long id) {
        Project project = findOwnedProjectOrThrow(id);
        return toResponse(project);
    }

    public ProjectResponse updateStatus(Long id, ProjectRequest request) {
        Project project = findOwnedProjectOrThrow(id);

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        if (request.getDueDate() != null) project.setDueDate(request.getDueDate());

        Project updated = projectRepository.save(project);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Project project = findOwnedProjectOrThrow(id);
        projectRepository.delete(project);
    }

    private Project findOwnedProjectOrThrow(Long id) {
        User currentUser = authUtil.getCurrentUser();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        return project;
    }

    private ProjectResponse toResponse(Project project) {
        boolean deadlineSoon = isDeadlineSoon(project.getDueDate());
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getEmail(),
                project.getStatus(),
                project.getDueDate(),
                deadlineSoon
        );
    }

    private boolean isDeadlineSoon(LocalDate dueDate) {
        if (dueDate == null) return false;
        LocalDate today = LocalDate.now();
        long daysUntilDue = today.until(dueDate).getDays();
        return !dueDate.isBefore(today) && daysUntilDue <= DEADLINE_SOON_DAYS;
    }
}