package com.greta.clicktalk.entities;


public class Setting {

    public Long id;
    public User userId;
    public String theme;

    public Setting() {}

    public Setting(User userId, String theme) {
        this.userId = userId;
        this.theme = theme;
    }

    public Setting(Long id, User userId, String theme) {
        this.id = id;
        this.userId = userId;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}