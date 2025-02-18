package com.greta.clicktalk.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}