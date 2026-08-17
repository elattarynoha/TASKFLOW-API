package com.nohaila.taskflow_api.service;

import com.nohaila.taskflow_api.dto.PagedResponse;
import com.nohaila.taskflow_api.dto.TaskRequest;
import com.nohaila.taskflow_api.dto.TaskResponse;
import com.nohaila.taskflow_api.entity.Project;
import com.nohaila.taskflow_api.entity.Task;
import com.nohaila.taskflow_api.entity.User;
import com.nohaila.taskflow_api.repository.ProjectRepository;
import com.nohaila.taskflow_api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import com.nohaila.taskflow_api.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.nohaila.taskflow_api.exception.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final AuthUtil authUtil;

    public TaskResponse create(Long projectId, TaskRequest request) {
        Project project = getOwnedProject(projectId);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        task.setProject(project);

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public List<TaskResponse> getTasksForProject(Long projectId) {
        getOwnedProject(projectId); // just to verify ownership/access
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskResponse updateStatus(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        getOwnedProject(task.getProject().getId()); // ownership check

        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());

        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    public void delete(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        getOwnedProject(task.getProject().getId());
        taskRepository.delete(task);
    }

    private Project getOwnedProject(Long projectId) {
        User currentUser = authUtil.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

       if (!project.getOwner().getId().equals(currentUser.getId())) {
        throw new AccessDeniedException("You do not have access to this project");
    }
        return project;
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject().getId()
        );
    }

public PagedResponse<TaskResponse> getTasksForProjectPaged(Long projectId, int page, int size, String sortBy, String direction) {
    getOwnedProject(projectId);

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Task> taskPage = taskRepository.findByProjectId(projectId, pageable);
    Page<TaskResponse> responsePage = taskPage.map(this::toResponse);

    return PagedResponse.of(responsePage);
}
}