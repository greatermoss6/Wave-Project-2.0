package com.signlearn.domain.service;

import com.signlearn.domain.model.Question;
import java.util.List;

public interface QuestionService {
    List<Question> questionsForLesson(long lessonId);
    boolean check(Question q, Object answer);
}