package com.signlearn.domain.model;

public class Lesson {
    private long id;
    private long chapterId;
    private String title;

    public Lesson(long id, long chapterId, String title) {
        this.id = id;
        this.chapterId = chapterId;
        this.title = title;
    }

    // convenience constructor for new lessons
    public Lesson(long chapterId, String title) {
        this(-1, chapterId, title);
    }

    public long getId() { return id; }
    public long getChapterId() { return chapterId; }
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    @Override
    public String toString() {
        return title != null ? title : ("Lesson #" + id);
    }
}