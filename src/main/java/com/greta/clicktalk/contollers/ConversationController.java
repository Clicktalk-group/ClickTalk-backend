package com.greta.clicktalk.contollers;

import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.entities.Conversation;
import com.greta.clicktalk.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("conversation")
public class ConversationController {
    private final ConversationDao conversationDao;
    private final UserDao userDao;

    public ConversationController(ConversationDao conversationDao, UserDao userDao) {
        this.conversationDao = conversationDao;
        this.userDao = userDao;
    }

    @GetMapping("all")
    public ResponseEntity<List<Conversation>> getAllConversations(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userDao.findByEmail(username);
        return conversationDao.getAllConversationsByUserId(currentUser.getId());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getConversationById(Authentication authentication, @PathVariable long id) {
        String username = authentication.getName();
        User currentUser = userDao.findByEmail(username);

      return  conversationDao.getConversationById(id, currentUser.getId());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteConversation(Authentication authentication,@PathVariable long id) {
        String email = authentication.getName();
        User currentUser = userDao.findByEmail(email);
        return  conversationDao.deleteConversation(id, currentUser.getId());
    }
}
