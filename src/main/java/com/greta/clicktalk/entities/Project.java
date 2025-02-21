package com.greta.clicktalk.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

public class Project {
    public long id;
    public String title;
    public String context;

    public Project() {
    }

    public Project(String title, String context) {
        this.title = title;
        this.context = context;
    }

    public Project(long id, String title, String context) {
        this.id = id;
        this.title = title;
        this.context = context;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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