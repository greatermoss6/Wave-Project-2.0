package com.signlearn.persistence.repo;

import com.signlearn.domain.model.Progress;
import java.util.List;
import java.util.Optional;

public interface ProgressRepository {
    Optional<Progress> find(long userId, long lessonId);
    List<Progress> findByUser(long userId);
    void upsert(Progress progress);
    void delete(long userId, long lessonId);
}