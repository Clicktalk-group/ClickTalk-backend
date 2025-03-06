package com.greta.clicktalk.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.DAOs.ProjectDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeepSeekService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${API_URL}")
    private String API_URL;
    @Value("${API_KEY}")
    private String API_KEY;

    private final ConversationDao conversationDao;
    private final ProjectDao projectDao;

    public DeepSeekService(ConversationDao conversationDao, ProjectDao projectDao) {
        this.conversationDao = conversationDao;
        this.projectDao = projectDao;
    }


    public String callDeepSeekAPI(Long conversationId, Long projectId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // Retrieve previous messages
        List<Map<String, Object>> previousMessages = conversationDao.getMessages(conversationId);

        // Construct the "messages" array
        List<Map<String, String>> messages = new ArrayList<>();
        if (projectId != null) {
            String projectContext = projectDao.getProjectContext(projectId);
            messages.add(Map.of("role", "system", "content", projectContext));
        }else {
            messages.add(Map.of("role", "system", "content", "You are a helpful assistant."));
        }

        for (Map<String, Object> msg : previousMessages) {
            String role = (Boolean) msg.get("is_bot") ? "assistant" : "user";
            messages.add(Map.of("role", role, "content", msg.get("content").toString()));
        }


        // Construct request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 20);

        HttpEntity< Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);
        return extractMessage(response.getBody());
    }

    public String generateTitleFromMessage(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of("role", "system", "content","Generate a short and catchy title for a conversation based on the following first user message. Keep it under 8 words and make it relevant and engaging without double quotes." ));
        messages.add(Map.of("role", "user", "content", message));


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 20);


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);
        return extractMessage(response.getBody());
    }


    private String extractMessage(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        }
}
