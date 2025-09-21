package com.signlearn.domain.model;

public class Video {
    private long lessonId;
    private String filePath;
    private int lengthSeconds;

    public Video(long lessonId, String filePath, int lengthSeconds) {
        this.lessonId = lessonId;
        this.filePath = filePath;
        this.lengthSeconds = lengthSeconds;
    }

    public long getLessonId() { return lessonId; }
    public String getFilePath() { return filePath; }
    public int getLengthSeconds() { return lengthSeconds; }

    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setLengthSeconds(int lengthSeconds) { this.lengthSeconds = lengthSeconds; }
}