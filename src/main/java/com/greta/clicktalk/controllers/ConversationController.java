package com.greta.clicktalk.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.entities.Conversation;
import com.greta.clicktalk.entities.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    @Operation(summary = "Get all conversations", description = "Retrieve all conversations for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Conversations retrieved successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "[{\"id\":1,\"userId\":1,\"title\":\"Conversation 1\",\"createdAt\":\"2025-03-04T10:00:00\"}]"))),
            @ApiResponse(responseCode = "204", description = "No conversations found", content = @Content(examples = @ExampleObject(name = "No conversations found", value = "[]")))
    })
    public ResponseEntity<List<Conversation>> getAllConversations(Authentication auth) {
        long userId = userDao.getUserIdFromAuth(auth);
        return conversationDao.getAllConversationsByUserId(userId);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get conversation by ID", description = "Retrieve a conversation by its ID for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Conversation retrieved successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "{\"id\":1,\"userId\":1,\"title\":\"Conversation 1\",\"createdAt\":\"2025-03-04T10:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Conversation not found", content = @Content(examples = @ExampleObject(name = "Conversation not found", value = "{\"error\":\"Conversation with id 1 not found\"}")))
    })
 public ResponseEntity<?> getConversationById(Authentication auth, @PathVariable long id) {
       long userId = userDao.getUserIdFromAuth(auth);
       return  conversationDao.getConversationById(id,userId);
    @Operation(summary = "Get conversations by project ID", description = "Retrieve all conversations for a specific project for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Conversations retrieved successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "[{\"id\":1,\"userId\":1,\"title\":\"Conversation 1\",\"createdAt\":\"2025-03-04T10:00:00\"}]"))),
            @ApiResponse(responseCode = "204", description = "No conversations found", content = @Content(examples = @ExampleObject(name = "No conversations found", value = "[]")))
    })
      public ResponseEntity<?> getConversationByProjectId(Authentication auth, @PathVariable("project-id") long projectId) {
        long userId = userDao.getUserIdFromAuth(auth);

        List<Conversation> conversations =conversationDao.getConversationsByProjectId(projectId,userId);

        return ResponseEntity.ok(conversations);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete a conversation", description = "Delete a conversation for the authenticated user.", responses = {
            @ApiResponse(responseCode = "204", description = "Conversation deleted successfully", content = @Content(examples = @ExampleObject(name = "Conversation deleted", value = ""))),
            @ApiResponse(responseCode = "404", description = "Conversation not found", content = @Content(examples = @ExampleObject(name = "Conversation not found", value = "{\"error\":\"Conversation with id 1 not found\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = @ExampleObject(name = "Internal server error", value = "{\"error\":\"Error while deleting conversation\"}")))
    })
   public ResponseEntity<String> deleteConversation(Authentication auth,@PathVariable long id) {
        long userId = userDao.getUserIdFromAuth(auth);

        return  conversationDao.deleteConversation(id,userId);
    }
}
