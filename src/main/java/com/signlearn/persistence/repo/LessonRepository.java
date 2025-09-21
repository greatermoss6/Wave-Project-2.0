package com.signlearn.persistence.repo;

import com.signlearn.domain.model.Lesson;
import java.util.List;
import java.util.Optional;

public interface LessonRepository {
    Optional<Lesson> findById(long id);
    List<Lesson> findByChapterId(long chapterId);
    long insert(Lesson lesson);
    void update(Lesson lesson);
    void delete(long id);
}