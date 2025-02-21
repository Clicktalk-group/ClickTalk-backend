package com.greta.clicktalk.contollers;

import com.greta.clicktalk.DAOs.MessageDao;
import com.greta.clicktalk.entities.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
public class MessageController {
    private final MessageDao messageDao;

    public MessageController(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMessage(@RequestBody Message message) {
        message.setIsBot(false);
        return messageDao.addMessage(message);
    }
}
