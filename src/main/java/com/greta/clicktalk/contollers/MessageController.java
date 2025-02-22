package com.greta.clicktalk.contollers;

import com.greta.clicktalk.DAOs.MessageDao;
import com.greta.clicktalk.DTOs.SendMessageRequestDTO;
import com.greta.clicktalk.entities.Message;
import com.greta.clicktalk.serveces.MessagesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("messages")
public class MessageController {
    private final MessagesService messagesService;

    public MessageController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping("conv/{id}")
    public ResponseEntity<?> getConversationMessages(@PathVariable("id") Long conversationId,Authentication auth) {
       return messagesService.getConversationMessages(conversationId, auth);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMessage(@RequestBody SendMessageRequestDTO sendMessageRequestDTO, Authentication auth) {
        return messagesService.addMessage(sendMessageRequestDTO, auth);
    }
}
