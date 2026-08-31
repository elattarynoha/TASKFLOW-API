package com.nohaila.taskflow_api.dto;

import com.nohaila.taskflow_api.entity.TaskPriority;
import com.nohaila.taskflow_api.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 150, message = "Title must be under 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must be under 1000 characters")
    private String description;

    private TaskStatus status;

    private TaskPriority priority;
}