package com.signlearn.domain.model;

public class Option {
    private final String text;
    private final boolean isCorrect;

    public Option(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() { return text; }
    public boolean isCorrect() { return isCorrect; }
}
