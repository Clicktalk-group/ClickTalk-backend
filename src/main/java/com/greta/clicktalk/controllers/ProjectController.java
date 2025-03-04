package com.greta.clicktalk.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greta.clicktalk.DAOs.ProjectDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.DTOs.ProjectResponseDTO;
import com.greta.clicktalk.entities.Project;
import com.greta.clicktalk.entities.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("project")
public class ProjectController {
    private final ProjectDao projectDao;
    private final UserDao userDao;

    public ProjectController(ProjectDao projectDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.userDao = userDao;
    }

    @GetMapping("all")
    @Operation(summary = "Get all projects", description = "Retrieve all projects for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "[{\"projectId\":1,\"title\":\"Project 1\",\"content\":\"Content of project 1\",\"conversations\":[]}]"))),
            @ApiResponse(responseCode = "204", description = "No projects found", content = @Content(examples = @ExampleObject(name = "No projects found", value = "[]")))
    })

    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects(Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        return projectDao.getAllProjectsByUserId(userId);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new project", description = "Add a new project for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Project added successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "{\"id\":1,\"userId\":1,\"title\":\"new project\",\"context\":\"You are a helpful assistant\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(examples = @ExampleObject(name = "Invalid request", value = "{\"error\":\"Invalid project data\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Example project", value = "{\"title\":\"new project\",\"context\":\"You are a helpful assistant\"}")))

    public ResponseEntity<Project> addProject(@RequestBody Project project, Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        project.setUserId(userId);
        return projectDao.addNewProject(project);
    }

    @PutMapping("update")
    @Operation(summary = "Update a project", description = "Update project details for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "{\"id\":1,\"userId\":1,\"title\":\"updated project\",\"context\":\"Updated context\"}"))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(examples = @ExampleObject(name = "Project not found", value = "{\"error\":\"Project with id 1 not found\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = @ExampleObject(name = "Internal server error", value = "{\"error\":\"Something went wrong while updating the project\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Example project", value = "{\"id\":\"1\", \"title\":\"new project\",\"context\":\"You are a helpful assistant\"}")))

    public ResponseEntity<?> updateProject(@RequestBody Project project, Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        project.setUserId(userId);
        return projectDao.updateProject(project);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete a project", description = "Delete a project for the authenticated user.", responses = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully", content = @Content(examples = @ExampleObject(name = "Project deleted", value = ""))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(examples = @ExampleObject(name = "Project not found", value = "{\"error\":\"Project with id 1 not found\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = @ExampleObject(name = "Internal server error", value = "{\"error\":\"Delete project failed\"}")))
    })

    public ResponseEntity<String> deleteProject(@PathVariable long id, Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        return projectDao.deleteProject(id, userId);
    }

    private long getUserIdFromAuthentication(Authentication auth) {
        String email = auth.getName();
        User currentUser = userDao.findByEmail(email);
        return currentUser.getId();
    }
}
