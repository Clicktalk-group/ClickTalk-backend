package com.greta.clicktalk.DAOs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.greta.clicktalk.DTOs.ProjectResponseDTO;
import com.greta.clicktalk.entities.Conversation;
import com.greta.clicktalk.entities.Project;
import com.greta.clicktalk.exceptions.ResourceNotFoundException;

@Repository
public class ProjectDao {
    private final JdbcTemplate jdbcTemplate;
    private final ConversationDao conversationDao;

    public ProjectDao(JdbcTemplate jdbcTemplate, ConversationDao conversationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.conversationDao = conversationDao;
    }

    private final RowMapper<Project> projectRowMapper = (rs, rowNum) -> new Project(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("title"),
            rs.getString("context"));

    public ResponseEntity<List<ProjectResponseDTO>> getAllProjectsByUserId(long userId) {
        String sql = "SELECT * FROM projects where user_id = ?";

        List<Project> projects = jdbcTemplate.query(sql, projectRowMapper, userId);

        List<ProjectResponseDTO> responseDTOS = new ArrayList<>();

        for (Project project : projects) {
            List<Conversation> conversations = conversationDao.getConversationsByProjectId(project.getId(), userId);
            responseDTOS.add(new ProjectResponseDTO(
                    project.getId(),
                    project.getTitle(),
                    project.getContext(),
                    conversations));
        }

        return projects.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(responseDTOS);
    }

    public String getProjectContext(Long projectId) {
        String sql = "SELECT context FROM projects where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, projectId);
    }

    public ResponseEntity<Project> addNewProject(Project project) {
        String sql = "INSERT INTO projects (user_id,title, context) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[] { "id" });
            ps.setLong(1, project.getUserId());
            ps.setString(2, project.getTitle());
            ps.setString(3, project.getContext());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            project.setId(keyHolder.getKey().longValue());
        }

        return ResponseEntity.ok(project);
    }

    public ResponseEntity<?> updateProject(Project project) {
        if (!existsById(project.getId())) {
            throw new ResourceNotFoundException("Project with id " + project.getId() + " not found");
        }
        String sql = "UPDATE projects SET title = ?, context = ? WHERE id = ? and user_id = ?";
        int rowAffected = jdbcTemplate.update(sql, project.getTitle(), project.getContext(), project.getId(),
                project.getUserId());
        return rowAffected > 0 ? ResponseEntity.ok(project)
                : ResponseEntity.internalServerError().body("Something went wrong while updating the project");
    }

    public ResponseEntity<String> deleteProject(long id, long userId) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Project with id " + id + " not found");
        }

        String sql = "DELETE FROM projects WHERE id = ? and user_id = ?";
        int rowAffected = jdbcTemplate.update(sql, id, userId);

        // reset the auto_increment for projects table
        jdbcTemplate.update("ALTER TABLE projects AUTO_INCREMENT = 0;");

        return rowAffected > 0 ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().body("Delete project failed");
    }

    private boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM projects WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
