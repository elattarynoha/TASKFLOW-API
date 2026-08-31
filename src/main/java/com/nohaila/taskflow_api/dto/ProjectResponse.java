package com.nohaila.taskflow_api.dto;

import com.nohaila.taskflow_api.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerEmail;
    private ProjectStatus status;
    private LocalDate dueDate;
    private boolean deadlineSoon;
}