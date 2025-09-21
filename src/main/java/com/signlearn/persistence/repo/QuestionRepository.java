package com.signlearn.persistence.repo;

import com.signlearn.domain.model.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    Optional<Question> findById(long id);
    List<Question> findByLessonId(long lessonId);
    long insert(Question question);
    void update(Question question);
    void delete(long id);
}