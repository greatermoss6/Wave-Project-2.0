package com.signlearn.domain.model;

import com.signlearn.domain.enums.QuestionType;
import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private List<ChoiceOption> options;
    private int correctIndex;

    public MultipleChoiceQuestion(long id, long lessonId, String prompt,
                                  List<ChoiceOption> options, int correctIndex) {
        super(id, lessonId, prompt);
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public List<ChoiceOption> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }

    public void setOptions(List<ChoiceOption> options) { this.options = options; }
    public void setCorrectIndex(int correctIndex) { this.correctIndex = correctIndex; }

    @Override
    public boolean checkAnswer(Object answer) {
        if (answer instanceof Integer idx) {
            return idx == correctIndex;
        }
        return false;
    }

    @Override
    public QuestionType type() {
        return QuestionType.MULTIPLE_CHOICE;
    }
}