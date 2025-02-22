package com.greta.clicktalk.DTOs;

public class SendMessageRequestDTO {
    private Long conversationId ;
    private String message;
    private Long projectId;

    public SendMessageRequestDTO(Long conversationId, String message, Long projectId) {
        this.conversationId = conversationId;
        this.message = message;
        this.projectId = projectId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
