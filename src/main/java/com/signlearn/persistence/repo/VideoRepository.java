package com.signlearn.persistence.repo;

import java.util.Optional;
import com.signlearn.domain.model.Video;

public interface VideoRepository {
    Optional<Video> findByLessonId(long lessonId);
    void insert(long lessonId, Video video);
    void update(long lessonId, Video video);
    void deleteByLessonId(long lessonId);
}