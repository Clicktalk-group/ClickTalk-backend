package com.greta.clicktalk.controllers;

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
    public ResponseEntity<List<Conversation>> getAllConversations(Authentication auth) {
        long userId = userDao.getUserIdFromAuth(auth);
        return conversationDao.getAllConversationsByUserId(userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getConversationById(Authentication auth, @PathVariable long id) {
       long userId = userDao.getUserIdFromAuth(auth);
       return  conversationDao.getConversationById(id,userId);
    }

    @GetMapping("project/{project-id}")
    public ResponseEntity<?> getConversationByProjectId(Authentication auth, @PathVariable("project-id") long projectId) {
        long userId = userDao.getUserIdFromAuth(auth);

        List<Conversation> conversations =conversationDao.getConversationsByProjectId(projectId,userId);
        return ResponseEntity.ok(conversations);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteConversation(Authentication auth,@PathVariable long id) {
        long userId = userDao.getUserIdFromAuth(auth);

        return  conversationDao.deleteConversation(id,userId);
    }
}
