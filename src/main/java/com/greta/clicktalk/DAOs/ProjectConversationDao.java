package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.ProjectConversation;
import com.greta.clicktalk.exceptions.ConversationAlreadyAssignedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectConversationDao {
    private final JdbcTemplate jdbcTemplate;

    public ProjectConversationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ProjectConversation> rowMapper = (rs,_)-> new ProjectConversation(
            rs.getLong("project_id"),
            rs.getLong("conv_id")
    );

    public void add(Long projectId, Long conversationId) {
        ProjectConversation projectConversation = exist(conversationId);
        if(projectConversation != null) {
            if(projectConversation.getProjectId() != projectId){
                throw new ConversationAlreadyAssignedException("conversation with id " + conversationId + " already assigned to project " + projectId);
            }
            return;
        }

        try {
            String sql = "INSERT INTO project_conversation (project_id, conv_id) VALUES (?, ?) ";
            jdbcTemplate.update(sql, projectId, conversationId);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Error: something went wrong while adding a conversation.");
        }
    }

    private ProjectConversation exist(long conversation_id) {
        String sql = "SELECT * FROM project_conversation WHERE conv_id = ?";

        return jdbcTemplate.query(sql, rowMapper, conversation_id).stream().findFirst().orElse(null);
    }
}
