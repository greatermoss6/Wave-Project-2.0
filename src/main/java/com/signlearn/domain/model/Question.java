package com.signlearn.domain.model;

import com.signlearn.domain.enums.QuestionType;

public abstract class Question {
    private final long id;
    private final long lessonId;
    private final String prompt;

    protected Question(long id, long lessonId, String prompt) {
        this.id = id;
        this.lessonId = lessonId;
        this.prompt = prompt;
    }

    public long getId() { return id; }
    public long getLessonId() { return lessonId; }
    public String getPrompt() { return prompt; }

    // Each subtype must define how it checks an answer
    public abstract boolean checkAnswer(Object answer);

    // Each subtype reports its kind
    public abstract QuestionType type();
}