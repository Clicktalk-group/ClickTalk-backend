package com.greta.clicktalk.controllers;

import com.greta.clicktalk.DAOs.ProjectDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.entities.Project;
import com.greta.clicktalk.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Project>> getAllProjects(Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        return projectDao.getAllProjectsByUserId(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project, Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        project.setUserId(userId);
        return projectDao.addNewProject(project);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateProject(@RequestBody Project project, Authentication auth) {
        long userId = getUserIdFromAuthentication(auth);
        project.setUserId(userId);
        return projectDao.updateProject(project);
    }

    @DeleteMapping("delete/{id}")
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
