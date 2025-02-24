package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.ProjectConversation;
import com.greta.clicktalk.excetions.ConversationAlreadyAssignedException;
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
        if(exist(conversationId)) {
            throw new ConversationAlreadyAssignedException("Conversation ID " + conversationId + " is already linked to a project.");
        }

        String sql = "INSERT INTO project_conversation (project_id, conv_id) VALUES (?, ?) ";
        jdbcTemplate.update(sql, projectId, conversationId);
    }

    private boolean exist(long conversation_id) {
        String sql = "SELECT COUNT(*) FROM project_conversation WHERE conv_id = ?";

        Integer counter = jdbcTemplate.queryForObject(sql, Integer.class, conversation_id);

        return counter != null && counter > 0;
    }
}
