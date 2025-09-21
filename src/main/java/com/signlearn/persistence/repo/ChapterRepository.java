package com.signlearn.persistence.repo;

import com.signlearn.domain.model.Chapter;
import java.util.List;
import java.util.Optional;

public interface ChapterRepository {
    Optional<Chapter> findById(long id);
    List<Chapter> findByModuleId(long moduleId);
    long insert(Chapter chapter);
    void update(Chapter chapter);
    void delete(long id);
}