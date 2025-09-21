package com.signlearn.domain.model;

public class Module {
    private long id;
    private String title;
    private String description;

    public Module(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return title != null ? title : ("Module #" + id);
    }
}