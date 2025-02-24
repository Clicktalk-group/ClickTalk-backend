package com.greta.clicktalk.services;

import com.greta.clicktalk.DAOs.ConversationDao;
import org.springframework.stereotype.Service;

@Service
public class GetAllConversationsService {
    private final ConversationDao conversationDao;

    public GetAllConversationsService(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }

}
