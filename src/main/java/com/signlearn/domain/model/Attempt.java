package com.signlearn.domain.model;

import java.time.Instant;
import java.util.Map;

public class Attempt {
    private long userId;
    private long lessonId;
    private Instant startedAt;
    private Instant submittedAt;
    private Map<Long, Integer> answersByQuestionId;

    public Attempt(long userId, long lessonId, Instant startedAt) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.startedAt = startedAt;
    }

    public long getUserId() { return userId; }
    public long getLessonId() { return lessonId; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getSubmittedAt() { return submittedAt; }
    public Map<Long, Integer> getAnswersByQuestionId() { return answersByQuestionId; }

    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    public void setAnswersByQuestionId(Map<Long, Integer> answersByQuestionId) {
        this.answersByQuestionId = answersByQuestionId;
    }
}