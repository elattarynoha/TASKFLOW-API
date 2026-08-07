package com.nohaila.taskflow_api.controller;

import com.nohaila.taskflow_api.dto.TaskRequest;
import com.nohaila.taskflow_api.dto.TaskResponse;
import com.nohaila.taskflow_api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/projects/{projectId}/tasks")
    public TaskResponse create(@PathVariable Long projectId, @RequestBody TaskRequest request) {
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
}