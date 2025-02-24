package com.greta.clicktalk.DTOs;

import com.greta.clicktalk.entities.Conversation;

import java.util.List;

public class ProjectResponseDTO {
    private long projectId;
    private String title;
    private String content;
    private List<Conversation> conversations;

    public ProjectResponseDTO(long projectId, String title, String content, List<Conversation> conversations) {
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.conversations = conversations;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }




}
