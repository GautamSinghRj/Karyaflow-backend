package com.example.karyaflow.requestbody.project;

import com.example.karyaflow.entity.Project;
import com.example.karyaflow.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ProjectCreationDetails {
    @NotBlank(message = "Project name is required")
    private String projectName;
    @NotBlank(message = "Name of the person who created the project is required")
    private String createdBy;
    @NotNull(message = "Creation Date is required")
    private LocalDate creationDate;
    private LocalDate completionDate;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Status of the project is needed")
    private Project.Status status;
    //Nested Class requires static
    public static class TeamDetails{
        private String departmentName;
        private List<String> memberNames=new ArrayList<>();
        public String getDepartmentName() {
            return departmentName;
        }
        public List<String> getMemberNames() {
            return memberNames;
        }

    }
    public static  class TaskDetails
    {
        private String taskName;
        private String createdBy;
        private LocalDate creationDate;
        private LocalDate completionDate;
        private String description;
        private Task.Status status;

        public String getTaskName() {
            return taskName;
        }

        public String getCreatedBy() {
            return createdBy;
        }

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
    }
    @NotEmpty(message = "At least one team is needed")
    private List<TeamDetails> teamDetails=new ArrayList<>();
    private List<TaskDetails> taskDetails=new ArrayList<>();

    public String getProjectName() {
        return projectName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDate getCreationDate() {
        return creationDate;
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

    public List<TeamDetails> getTeamDetails() {
        return teamDetails;
    }

    public List<TaskDetails> getTaskDetails() {
        return taskDetails;
    }
}
