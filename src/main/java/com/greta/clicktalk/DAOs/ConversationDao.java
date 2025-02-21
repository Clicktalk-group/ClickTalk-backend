package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.Conversation;
import com.greta.clicktalk.excetions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ConversationDao {
    private final JdbcTemplate jdbcTemplate;

    public ConversationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Conversation> conversationRowMapper = (rs,_)-> new Conversation(
        rs.getLong("id"),
        rs.getLong("user_id"),
        rs.getString("title"),
        rs.getString("created_at")
    );

    public ResponseEntity<List<Conversation>> getAllConversationsByUserId(long userId) {
        String sql = "SELECT * FROM conversations WHERE user_id=?";
        List<Conversation> conversations = jdbcTemplate.query(sql, conversationRowMapper, userId);
        return conversations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(conversations);
    }

    public ResponseEntity<?> getConversationById(long conversationId,long userId) {
        if(!existsById(conversationId)) {
            throw new ResourceNotFoundException("Conversation with id " + conversationId + " not found");
        }
        String sql = "SELECT * FROM conversations WHERE id=? AND user_id=?  ";

        Conversation conversation = jdbcTemplate.queryForObject(sql, conversationRowMapper,conversationId, userId);
        return conversation == null ? ResponseEntity.internalServerError().body("something went wrong") : ResponseEntity.ok(conversation);
    }

    public ResponseEntity<Conversation> addConversation(Conversation conversation) {
        String sql = "INSERT INTO conversations (user_id, title,created_at) VALUES (?, ?, ?)";
        String now = LocalDateTime.now().toString();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, conversation.getUserId());
            ps.setString(2, conversation.getTitle());
            ps.setString(3, now);
            return ps;
        }, keyHolder);

        conversation.setCreatedAt(now);

        if (keyHolder.getKey() != null) {
            conversation.setId(keyHolder.getKey().longValue());
        }

        return ResponseEntity.ok(conversation);
    }

    public ResponseEntity<String> deleteConversation(long conversationId,long userId) {
        if (!existsById(conversationId)) {
            throw new ResourceNotFoundException("Conversation with id " + conversationId + " not found");
        }
try {
    String sql = "DELETE FROM conversations WHERE id=? AND user_id = ?";
    int rowAffected = jdbcTemplate.update(sql, conversationId, userId);

    // reset the auto_increment for conversations table
    jdbcTemplate.update("ALTER TABLE conversations AUTO_INCREMENT = 0;");
    return rowAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.internalServerError().body("Error: something went wrong while deleting a conversation");
}
catch (Exception e) {
    System.out.println(e.getMessage());
}
return ResponseEntity.noContent().build();
    }

    public boolean existsById(long conversationId) {
         String sql = "SELECT COUNT(*) FROM conversations WHERE id=?";
        Integer conversationsNumber = jdbcTemplate.queryForObject(sql, Integer.class, conversationId);
        return conversationsNumber != null && conversationsNumber > 0;
    }
}
