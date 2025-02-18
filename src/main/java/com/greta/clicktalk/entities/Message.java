package com.greta.clicktalk.entities;


public class Message {
    public Long id;
    public Conversation conv;
    public String content;
    public Boolean isBot;
    public String createdAt;

    public Message() {
    }

    public Message(Conversation conv, String content, Boolean isBot) {
        this.conv = conv;
        this.content = content;
        this.isBot = isBot;
    }

    public Message( Long id, Conversation conv, String content, Boolean isBot, String createdAt) {
        this.id = id;
        this.conv = conv;
        this.content = content;
        this.isBot = isBot;
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConv() {
        return conv;
    }

    public void setConv(Conversation conv) {
        this.conv = conv;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsBot() {
        return isBot;
    }

    public void setIsBot(Boolean isBot) {
        this.isBot = isBot;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}