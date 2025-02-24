package com.greta.clicktalk.controllers;

import com.greta.clicktalk.DAOs.MessageDao;
import com.greta.clicktalk.DTOs.SendMessageRequestDTO;
import com.greta.clicktalk.services.DeepSeekStreamingService;
import com.greta.clicktalk.services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("deepseek/")
public class DeepSeekController {

    @Autowired
    private DeepSeekStreamingService deepSeekService;

    @Autowired
    private MessageDao messageDao;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamData(@RequestBody SendMessageRequestDTO sendMessageRequestDTO, Authentication auth) {
         messageDao.addMessage(sendMessageRequestDTO.getConversationId(),sendMessageRequestDTO.getMessage(),false);
         return deepSeekService.streamData(sendMessageRequestDTO.getConversationId(), sendMessageRequestDTO.getProjectId());
    }
}
