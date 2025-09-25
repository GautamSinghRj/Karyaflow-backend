package com.example.karyaflow.controller;

import com.example.karyaflow.entity.Project;
import com.example.karyaflow.requestbody.project.ProjectCreationDetails;
import com.example.karyaflow.requestbody.project.ProjectDeletionDetails;
import com.example.karyaflow.requestbody.project.ProjectUpdationDetails;
import com.example.karyaflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping("/createProject")
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@Valid @RequestBody ProjectCreationDetails obj){
        return service.createProject(obj);
    }

    @GetMapping("/listProjects")
    @ResponseStatus(HttpStatus.OK)
    public List<Project> getProject(){
        return service.getProject();
    }

    @PatchMapping("/updateProject")
    @ResponseStatus(HttpStatus.OK)
    public Project updateProject(@Valid @RequestBody ProjectUpdationDetails obj){
        return service.updateProject(obj);
    }

    @DeleteMapping("/deleteProject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@RequestBody ProjectDeletionDetails obj) {
        service.deleteProject(obj);
    }
}
