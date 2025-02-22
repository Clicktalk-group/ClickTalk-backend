package com.greta.clicktalk.serveces;

import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.entities.Conversation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllConversationsService {
    private final ConversationDao conversationDao;

    public GetAllConversationsService(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }

}
