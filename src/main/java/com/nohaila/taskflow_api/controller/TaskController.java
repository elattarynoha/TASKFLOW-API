package com.nohaila.taskflow_api.controller;

import com.nohaila.taskflow_api.dto.PagedResponse;
import com.nohaila.taskflow_api.dto.TaskRequest;
import com.nohaila.taskflow_api.dto.TaskResponse;
import com.nohaila.taskflow_api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import com.nohaila.taskflow_api.dto.PagedResponse;
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/projects/{projectId}/tasks")
    public TaskResponse create(@PathVariable Long projectId, @Valid @RequestBody TaskRequest request) {
        return taskService.create(projectId, request);
    }

    @GetMapping("/api/projects/{projectId}/tasks")
    public List<TaskResponse> getTasksForProject(@PathVariable Long projectId) {
        return taskService.getTasksForProject(projectId);
    }

    @PatchMapping("/api/tasks/{taskId}")
    public TaskResponse update(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        return taskService.updateStatus(taskId, request);
    }

    @DeleteMapping("/api/tasks/{taskId}")
    public void delete(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }
    @GetMapping("/api/projects/{projectId}/tasks/paged")
public PagedResponse<TaskResponse> getTasksPaged(
        @PathVariable Long projectId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
) {
    return taskService.getTasksForProjectPaged(projectId, page, size, sortBy, direction);
}
}