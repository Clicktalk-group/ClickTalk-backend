package com.greta.clicktalk.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greta.clicktalk.DTOs.SendMessageRequestDTO;
import com.greta.clicktalk.services.MessagesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("messages")
public class MessageController {
    private final MessagesService messagesService;

    public MessageController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping("conv/{id}")
    @Operation(summary = "Get conversation messages", description = "Retrieve all messages for a specific conversation for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "[{\"id\":1,\"convId\":1,\"content\":\"Hello\",\"isBot\":false,\"createdAt\":\"2025-03-04T10:00:00\"}]"))),
            @ApiResponse(responseCode = "204", description = "No messages found", content = @Content(examples = @ExampleObject(name = "No messages found", value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(examples = @ExampleObject(name = "Forbidden", value = "You do not have permission to access these messages"))),
            @ApiResponse(responseCode = "404", description = "Conversation not found", content = @Content(examples = @ExampleObject(name = "Conversation not found", value = "Conversation with id 1 not found")))
    })
    public ResponseEntity<?> getConversationMessages(@PathVariable("id") Long conversationId, Authentication auth) {
        return messagesService.getConversationMessages(conversationId, auth);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a message", description = "Add a new message to a conversation for the authenticated user. If `conversationId` is not provided, a new conversation will be created. and if `projectId` in not provided the conversation will be general", responses = {
            @ApiResponse(responseCode = "200", description = "Message added successfully", content = @Content(examples = {
                    @ExampleObject(name = "Example response", value = "{\"id\":1,\"convId\":1,\"content\":\"Hello\",\"isBot\":false,\"createdAt\":\"2025-03-04T10:00:00\"}")
            })),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(examples = {
                    @ExampleObject(name = "Invalid request", value = "Error: Invalid message data")
            })),
            @ApiResponse(responseCode = "404", description = "Conversation not found", content = @Content(examples = {
                    @ExampleObject(name = "Conversation not found", value = "Conversation with id 1 not found")
            }))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(name = "Only message", value = "{\"message\":\"Hello\"}"),
            @ExampleObject(name = "Message with conversationId", value = "{\"conversationId\":1,\"message\":\"Hello\"}"),
            @ExampleObject(name = "Message with conversationId and projectId", value = "{\"conversationId\":1,\"message\":\"Hello\",\"projectId\":1}")
    }))
    public ResponseEntity<?> addMessage(@RequestBody SendMessageRequestDTO sendMessageRequestDTO, Authentication auth) {
        return messagesService.addMessage(sendMessageRequestDTO, auth);
    }
}
