package com.greta.clicktalk.entities;


public class Setting {
    private long id;
    private long userId;
    private String theme;

    public Setting() {}

    public Setting(long userId, String theme) {
        this.userId = userId;
        this.theme = theme;
    }

    public Setting(long id, long userId, String theme) {
        this.id = id;
        this.userId = userId;
        this.theme = theme;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}