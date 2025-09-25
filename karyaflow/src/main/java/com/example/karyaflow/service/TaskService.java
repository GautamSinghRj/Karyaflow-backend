package com.example.karyaflow.service;

import com.example.karyaflow.entity.Project;
import com.example.karyaflow.entity.Task;
import com.example.karyaflow.repo.ProjectRepo;
import com.example.karyaflow.repo.TaskRepo;
import com.example.karyaflow.requestbody.task.TaskCreationDetails;
import com.example.karyaflow.requestbody.task.TaskDeletionDetails;
import com.example.karyaflow.requestbody.task.TaskUpdationDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {
    private TaskRepo repo;
    private ProjectRepo projectRepo;

    public TaskService(TaskRepo repo, ProjectRepo projectRepo) {
        this.repo = repo;
        this.projectRepo = projectRepo;
    }

    public List<Task> getTasks() {
        return repo.findAll();
    }

    public void deleteTask(TaskDeletionDetails obj) {
        Task task=repo.findById(obj.getId()).orElse(null);
        if (task!=null){
            repo.delete(task);
        }
        else {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");}
    }

    public Task createTask(@Valid TaskCreationDetails obj) {
        Task task=new Task();
        task.setTaskName(obj.getTaskName());
        task.setCreatedBy(obj.getCreatedBy());
        task.setCreationDate(obj.getCreationDate());
        task.setCompletionDate(obj.getCompletionDate());
        task.setStatus(obj.getStatus());
        Project project=new Project();
        project=projectRepo.findById(obj.getProjectId()).orElse(null);
        if (project!=null){task.setProject(project);}
        else {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");}
        repo.save(task);
        return task;
    }

    public Task updateTask(@Valid TaskUpdationDetails obj) {
        Task task=repo.findById(obj.getId()).orElse(null);
        if(task!=null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found in the database");
        }
        else
        {
            task.setTaskName(obj.getTaskName());
            task.setDescription(obj.getDescription());
            task.setCompletionDate(obj.getCompletionDate());
            task.setStatus(obj.getStatus());
        }
        repo.save(task);
        return task;
    }
}
