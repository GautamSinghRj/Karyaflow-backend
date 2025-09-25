package com.example.karyaflow.controller;

import com.example.karyaflow.entity.Task;
import com.example.karyaflow.requestbody.task.TaskCreationDetails;
import com.example.karyaflow.requestbody.task.TaskDeletionDetails;
import com.example.karyaflow.requestbody.task.TaskUpdationDetails;
import com.example.karyaflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/listTasks")
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTasks(){
        return service.getTasks();
    }

    @PostMapping("/createTask")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody TaskCreationDetails obj){
        return service.createTask(obj);
    }

    @PatchMapping("/updateTask")
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@Valid @RequestBody TaskUpdationDetails obj){
        return service.updateTask(obj);
    }

    @DeleteMapping("/deleteTask")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@RequestBody TaskDeletionDetails obj){
        service.deleteTask(obj);
    }
}
