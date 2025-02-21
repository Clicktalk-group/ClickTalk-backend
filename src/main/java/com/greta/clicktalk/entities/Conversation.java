package com.greta.clicktalk.entities;

public class Conversation {
    private long id;
    private long userId;
    private String title;
    private String createdAt;

    public Conversation() {
    }

    public Conversation(long userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    public Conversation(long id, long userId, String title, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}