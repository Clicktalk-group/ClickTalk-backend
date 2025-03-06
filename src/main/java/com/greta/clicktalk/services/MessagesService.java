package com.greta.clicktalk.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.DAOs.MessageDao;
import com.greta.clicktalk.DAOs.ProjectConversationDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.DTOs.SendMessageRequestDTO;
import com.greta.clicktalk.entities.Conversation;
import com.greta.clicktalk.entities.User;

@Service
public class MessagesService {
    private final MessageDao messageDao;
    private final ConversationDao conversationDao;
    private final UserDao userDao;
    private final ProjectConversationDao projectConversationDao;
    private final DeepSeekService deepSeekService;

    public MessagesService(MessageDao messageDao, ConversationDao conversationDao, UserDao userDao,
            ProjectConversationDao projectConversationDao, DeepSeekService deepSeekService) {
        this.messageDao = messageDao;
        this.conversationDao = conversationDao;
        this.userDao = userDao;
        this.projectConversationDao = projectConversationDao;
        this.deepSeekService = deepSeekService;
    }

    public ResponseEntity<?> addMessage(SendMessageRequestDTO sendMessageRequestDTO, Authentication auth) {

        // get the current user id
        long userId = userDao.getUserIdFromAuth(auth);
        Long projectId =  sendMessageRequestDTO.getProjectId();
        Long conversationId =  sendMessageRequestDTO.getConversationId();

        // get the project id and conversation id from the request
        Long projectId = sendMessageRequestDTO.getProjectId();
        Long conversationId = sendMessageRequestDTO.getConversationId();

        // if the conversation id is not provided, create a new conversation
        if (conversationId == null) {
            // generate the title for the first message
            String title = deepSeekService.generateTitleFromMessage(sendMessageRequestDTO.getMessage());

            // add the conversation to the database
            Conversation conversation = new Conversation(
                    userId,
                    title);
            conversationId = conversationDao.addConversation(conversation).getId();
        }

        // if the project id is provided, add the conversation to the project
        if (projectId != null) {
            projectConversationDao.add(projectId, conversationId);
        }

        // add the user message to the database
        messageDao.addMessage(conversationId, sendMessageRequestDTO.getMessage(), false);

        // call the deep seek API and get the response message
        String response = deepSeekService.callDeepSeekAPI(conversationId, projectId);

        // add the response message to the database and return it
        return messageDao.addMessage(conversationId, response, true);
    }

    public ResponseEntity<?> getConversationMessages(Long conversationId, Authentication auth) {
         long userId = userDao.getUserIdFromAuth(auth);
        return  messageDao.getConversationMessages(conversationId,userId);
    }
}
