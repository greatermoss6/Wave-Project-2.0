package com.signlearn.domain.model;

public class ChoiceOption {
    private int index;
    private String text;
    private boolean correct;

    public ChoiceOption(int index, String text, boolean correct) {
        this.index = index;
        this.text = text;
        this.correct = correct;
    }

    public int getIndex() { return index; }
    public String getText() { return text; }
    public boolean isCorrect() { return correct; }

    public void setText(String text) { this.text = text; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}