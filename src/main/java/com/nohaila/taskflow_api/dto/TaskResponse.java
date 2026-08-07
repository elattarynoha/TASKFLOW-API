package com.nohaila.taskflow_api.dto;

import com.nohaila.taskflow_api.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long projectId;
}