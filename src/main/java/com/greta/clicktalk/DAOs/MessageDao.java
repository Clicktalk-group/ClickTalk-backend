package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.Message;
import com.greta.clicktalk.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MessageDao {
    private final JdbcTemplate jdbcTemplate;
    private final ConversationDao conversationDao;

    public MessageDao(JdbcTemplate jdbcTemplate, ConversationDao conversationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.conversationDao = conversationDao;
    }

    private final RowMapper<Message> messageRowMapper = (rs,_)->new Message(
            rs.getLong("id"),
            rs.getLong("conv_id"),
            rs.getString("content"),
            rs.getBoolean("is_bot"),
            rs.getString("created_at")
    );

    public ResponseEntity<List<Message>> getConversationMessages(long conversationId,long userId) {

        // check if the conversation exist and get his ownerId
        if(conversationDao.existsById(conversationId)){
            String checkQuery = "SELECT user_id FROM conversations WHERE id = ?";

            Long ownerId = jdbcTemplate.queryForObject(checkQuery, Long.class, conversationId);
            if(ownerId != null && !ownerId.equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else{
            throw new ResourceNotFoundException("conversation with ID: " + conversationId + " dose not exist");
        }

        String query = "SELECT * FROM messages WHERE conv_id=?";
        List<Message> messages = jdbcTemplate.query(query, messageRowMapper, conversationId);
        return messages.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(messages);
    }

    public ResponseEntity<?> addMessage(long conversationId, String content, boolean isBot) {
        if(!conversationDao.existsById(conversationId)){
            throw new ResourceNotFoundException("Conversation with id "+conversationId+" does not exist");
        }

        String sql = "INSERT INTO messages (conv_id,content,is_bot,created_at) VALUES (?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String now = LocalDateTime.now().toString();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[] { "id" });
            ps.setLong(1, conversationId);
            ps.setString(2, content);
            ps.setBoolean(3, isBot);
            ps.setString(4, now);
            return ps;
        }, keyHolder);

        if(keyHolder.getKey() != null) {
            Message message = new Message(
                    keyHolder.getKey().longValue(),
                    conversationId,
                    content,
                    isBot,
                    now
            );
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.internalServerError().body("something went wrong while adding message");

    }
}
