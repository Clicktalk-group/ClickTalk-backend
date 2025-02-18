package com.greta.clicktalk.entities;

public class Conversation {
    public Long id;
    public User user;
    public String title;
    public String createdAt;

    public Conversation() {
    }

    public Conversation(User user, String title) {
        this.user = user;
        this.title = title;
    }

    public Conversation(Long id, User user, String title, String createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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