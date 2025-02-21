package com.greta.clicktalk.entities;

public class ProjectConversation {
    public long projectId;
    public long convId;

    public ProjectConversation() {
    }

    public ProjectConversation(long projectId, long convId) {
        this.projectId = projectId;
        this.convId = convId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getConvId() {
        return convId;
    }

    public void setConvId(long convId) {
        this.convId = convId;
    }

}