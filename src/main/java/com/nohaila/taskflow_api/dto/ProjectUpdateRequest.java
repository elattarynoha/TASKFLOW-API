package com.nohaila.taskflow_api.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectUpdateRequest {

    @Size(max = 100, message = "Project name must be under 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be under 500 characters")
    private String description;

    private LocalDate startDate;

    private LocalDate dueDate;
}