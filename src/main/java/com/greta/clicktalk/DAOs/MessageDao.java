package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.Message;
import com.greta.clicktalk.excetions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    public ResponseEntity<List<Message>> getConversationMessages(long conversationId) {
        String query = "SELECT * FROM messages WHERE conv_id=?";

        List<Message> messages = jdbcTemplate.query(query, messageRowMapper, conversationId);
        return messages.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(messages);
    }

    public ResponseEntity<String> addMessage(Message message) {
        if(!conversationDao.existsById(message.getConvId())){
            throw new ResourceNotFoundException("Conversation with id "+message.getConvId()+" does not exist");
        }
        String sql = "INSERT INTO messages (conv_id,content,is_bot) VALUES (?,?,?);";
        try{
            jdbcTemplate.update(sql,message.getConvId(),message.getContent(),message.getIsBot());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("Message added");
    }
}
