package com.nohaila.taskflow_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerEmail;
}