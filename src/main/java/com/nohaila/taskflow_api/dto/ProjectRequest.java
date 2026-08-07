package com.nohaila.taskflow_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {
    private String name;
    private String description;
}