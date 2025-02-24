package com.greta.clicktalk.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greta.clicktalk.DAOs.ConversationDao;
import com.greta.clicktalk.DAOs.ProjectDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeepSeekStreamingService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${API_URL}")
    private String API_URL;
    @Value("${API_KEY}")
    private String API_KEY;

    private final ConversationDao conversationDao;
    private final ProjectDao projectDao;

    public DeepSeekStreamingService(ConversationDao conversationDao, ProjectDao projectDao) {
        this.conversationDao = conversationDao;
        this.projectDao = projectDao;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.deepseek.com/")
                .defaultHeader("Authorization", "Bearer " + API_KEY)
                .build();
    }

    public Flux<String> streamData(Long conversationId, Long projectId) {
        // Retrieve previous messages
        List<Map<String, Object>> previousMessages = conversationDao.getMessages(conversationId);

        // Construct the "messages" array
        List<Map<String, String>> messages = new ArrayList<>();
        if (projectId != null) {
            String projectContext = projectDao.getProjectContext(projectId);
            messages.add(Map.of("role", "system", "content", projectContext));
        } else {
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
        requestBody.put("max_tokens", 100);
        requestBody.put("stream",true);


        return webClient.post()
                .uri("chat/completions") // specify the endpoint path if needed
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + API_KEY)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::extractMessage)
                .filter(message -> !message.isEmpty());
    }



        private String extractMessage(String responseBody) {
            System.out.println("**********************************************************88");
            System.out.println(responseBody);
            System.out.println("**********************************************************88");
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                // Check if "choices" array is not empty
                if (jsonNode.has("choices") && jsonNode.get("choices").size() > 0) {
                    JsonNode deltaNode = jsonNode.path("choices").get(0).path("delta");
                    // Extract the content from the "delta" node
                    String content = deltaNode.path("content").asText();
                    // Return the content; if it's empty, return an empty string
                    return content; // No need to check for empty and return null
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return ""; // Return an empty string if no valid content is found
        }


}
