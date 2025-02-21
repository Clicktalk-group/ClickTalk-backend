package com.greta.clicktalk.entities;


public class Message {
    public long id;
    public long convId;
    public String content;
    public Boolean isBot;
    public String createdAt;

    public Message() {
    }

    public Message(long convId, String content, Boolean isBot) {
        this.convId = convId;
        this.content = content;
        this.isBot = isBot;
    }


    public Message( long id, long convId, String content, Boolean isBot, String createdAt) {
        this.id = id;
        this.convId = convId;
        this.content = content;
        this.isBot = isBot;
        this.createdAt = createdAt;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConvId() {
        return convId;
    }

    public void setConvId(long convId) {
        this.convId = convId;
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