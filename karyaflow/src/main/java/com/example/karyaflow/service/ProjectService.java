package com.example.karyaflow.service;

import com.example.karyaflow.entity.Project;
import com.example.karyaflow.entity.Task;
import com.example.karyaflow.entity.Team;
import com.example.karyaflow.entity.TeamMember;
import com.example.karyaflow.repo.ProjectRepo;
import com.example.karyaflow.requestbody.project.ProjectCreationDetails;
import com.example.karyaflow.requestbody.project.ProjectDeletionDetails;
import com.example.karyaflow.requestbody.project.ProjectUpdationDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo repo;

    public ProjectService(ProjectRepo repo) {
        this.repo = repo;
    }

    public Project createProject(ProjectCreationDetails obj){
        Project project=new Project();
        if(obj.getTeamDetails() !=null && !obj.getTeamDetails().isEmpty())
        {
            List<Team> teamList=obj.getTeamDetails().stream().map(
                    details->{
                        Team team=new Team();
                        team.setDepartmentName(details.getDepartmentName());
                        team.setMembers(details.getMemberNames().stream().map
                                (name-> {
                                    TeamMember teamMember=new TeamMember();
                                    teamMember.setMemberName(name);
                                    teamMember.setTeam(team);
                                    return teamMember;
                                }).toList());
                        return team;
                    }
            ).toList();
            //doing for the inverse side
            teamList.forEach(team -> team.getProjects().add(project));
            project.setTeams(teamList);
        }
        if(obj.getTaskDetails() != null && !obj.getTaskDetails().isEmpty())
        {
            List<Task> taskList=obj.getTaskDetails().stream().map(
                    details->{
                        Task task=new Task();
                        task.setTaskName(details.getTaskName());
                        task.setCreatedBy(details.getCreatedBy());
                        task.setCreationDate(details.getCreationDate());
                        task.setCompletionDate(details.getCompletionDate());
                        task.setDescription(details.getDescription());
                        task.setStatus(details.getStatus());
                        task.setProject(project);
                        return task;
                    }
            ).toList();
            project.setTasks(taskList);
        }
        project.setProjectName(obj.getProjectName());
        project.setCreatedBy(obj.getCreatedBy());
        project.setCreationDate(obj.getCreationDate());
        project.setCompletionDate(obj.getCompletionDate());
        project.setDescription(obj.getDescription());
        project.setStatus(obj.getStatus());
        repo.save(project);
        return project;
    }

    public List<Project> getProject(){
        return repo.findAll();
    }

    public void deleteProject(ProjectDeletionDetails obj) {
        Project project=repo.findById(obj.getId()).orElse(null);
        if(project !=null){repo.delete(project);}
        else {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");}
    }

    public Project updateProject(ProjectUpdationDetails obj) {
        Project project=repo.findById(obj.getId()).orElse(null);
        if(project == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Project not found in the database");
        }
        else
        {
            project.setCompletionDate(obj.getCompletionDate());
            project.setDescription(obj.getDescription());
            project.setProjectName(obj.getProjectName());
            project.setStatus(obj.getStatus());
        }
        repo.save(project);
        return project;
    }
}
