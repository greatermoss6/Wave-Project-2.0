package com.signlearn.domain.model;

public class Progress {
    private long userId;
    private long lessonId;
    private boolean completed;
    private Integer scorePercent;

    public Progress(long userId, long lessonId, boolean completed, Integer scorePercent) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.completed = completed;
        this.scorePercent = scorePercent;
    }

    public long getUserId() { return userId; }
    public long getLessonId() { return lessonId; }
    public boolean isCompleted() { return completed; }
    public Integer getScorePercent() { return scorePercent; }

    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setScorePercent(Integer scorePercent) { this.scorePercent = scorePercent; }
}