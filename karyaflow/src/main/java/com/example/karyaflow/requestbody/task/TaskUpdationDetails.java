package com.example.karyaflow.requestbody.task;

import com.example.karyaflow.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TaskUpdationDetails {
    @NotNull
    private Long id;
    @NotBlank(message = "Task name is required")
    private String taskName;
    private LocalDate completionDate;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Status of the task is needed")
    private Task.Status status;

    public @NotNull Long getId() {
        return id;
    }

    public @NotBlank(message = "Task name is required") String getTaskName() {
        return taskName;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public @NotBlank(message = "Description is required") String getDescription() {
        return description;
    }

    public @NotNull(message = "Status of the task is needed") Task.Status getStatus() {
        return status;
    }
}
