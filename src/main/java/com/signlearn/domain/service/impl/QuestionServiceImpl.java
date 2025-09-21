package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.Question;
import com.signlearn.domain.service.QuestionService;
import com.signlearn.persistence.repo.QuestionRepository;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepo;

    public QuestionServiceImpl(QuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public List<Question> questionsForLesson(long lessonId) {
        return questionRepo.findByLessonId(lessonId);
    }

    @Override
    public boolean check(Question q, Object answer) {
        return q.checkAnswer(answer);
    }
}