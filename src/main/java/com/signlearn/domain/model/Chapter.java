package com.signlearn.domain.model;

public class Chapter {
    private long id;
    private long moduleId;
    private String title;

    public Chapter(long id, long moduleId, String title) {
        this.id = id;
        this.moduleId = moduleId;
        this.title = title;
    }

    public long getId() { return id; }
    public long getModuleId() { return moduleId; }
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    @Override
    public String toString() {
        return title != null ? title : ("Chapter #" + id);
    }
}