package com.example.karyaflow.requestbody.task;

import com.example.karyaflow.entity.Task;

import java.time.LocalDate;

public class TaskCreationDetails {
    private String taskName;
    private String createdBy;
    private LocalDate creationDate;
    private LocalDate completionDate;
    private String description;
    private Task.Status status;
    private Long projectId;

    public String getTaskName() {
        return taskName;
    }

    public String getCreatedBy() {return createdBy;}

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public String getDescription() {
        return description;
    }

    public Task.Status getStatus() {
        return status;
    }

    public Long getProjectId() {return projectId;}
}
