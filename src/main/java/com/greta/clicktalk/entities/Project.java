package com.greta.clicktalk.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

public class Project {
    private long id;
    private long userId;
    private String title;
    private String context;

    public Project() {
    }

    public Project(long userId, String title, String context) {
        this.userId = userId;
        this.title = title;
        this.context = context;
    }

    public Project(long id,long userId, String title, String context) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.context = context;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}