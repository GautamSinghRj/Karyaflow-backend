package com.example.karyaflow.requestbody.project;

import com.example.karyaflow.entity.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ProjectUpdationDetails {
    @NotNull
    private Long id;
    @NotBlank(message = "Project name is required")
    private String projectName;
    private LocalDate completionDate;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Status of the project is needed")
    private Project.Status status;

    public String getProjectName() {
        return projectName;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public String getDescription() {
        return description;
    }

    public Project.Status getStatus() {
        return status;
    }

    public @NotNull Long getId() {
        return id;
    }
}
