package com.greta.clicktalk.serveces;

import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.DAOs.MessageDao;
import com.greta.clicktalk.DAOs.ProjectConversationDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.DTOs.SendMessageRequestDTO;
import com.greta.clicktalk.entities.Conversation;
import com.greta.clicktalk.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {
    private final MessageDao messageDao;
    private final ConversationDao conversationDao;
    private final UserDao userDao;
    private final ProjectConversationDao projectConversationDao;
    private final DeepSeekService deepSeekService;


    public MessagesService(MessageDao messageDao, ConversationDao conversationDao, UserDao userDao, ProjectConversationDao projectConversationDao, DeepSeekService deepSeekService) {
        this.messageDao = messageDao;
        this.conversationDao = conversationDao;
        this.userDao = userDao;
        this.projectConversationDao = projectConversationDao;
        this.deepSeekService = deepSeekService;
    }

    public ResponseEntity<?> addMessage(SendMessageRequestDTO sendMessageRequestDTO, Authentication auth) {

        // get the current user id
        long userId = getUserIdFromAuth(auth);
        Long projectId =  sendMessageRequestDTO.getProjectId();
        Long conversationId =  sendMessageRequestDTO.getConversationId();

        if(conversationId == null){
            String title = deepSeekService.generateTitleFromMessage(sendMessageRequestDTO.getMessage());
            Conversation conversation = new Conversation(
                    userId,
                    title
            );
         conversationId = conversationDao.addConversation(conversation).getId();
        }


        if(projectId != null){
        projectConversationDao.add(projectId, conversationId);
        }

        messageDao.addMessage(conversationId,sendMessageRequestDTO.getMessage(),false);

        String response = deepSeekService.callDeepSeekAPI(conversationId,projectId);
        return ResponseEntity.ok(messageDao.addMessage(conversationId,response,true)
);

    }

    public ResponseEntity<?> getConversationMessages(Long conversationId, Authentication auth) {
        long userId = getUserIdFromAuth(auth);
        return  messageDao.getConversationMessages(conversationId,userId);
    }

    private long getUserIdFromAuth(Authentication auth) {
        if(auth == null){
            throw new IllegalStateException("the user is not logged in");
        }
        String email = auth.getName();
        User currentUser = userDao.findByEmail(email);
        return currentUser.getId();
    }
}
