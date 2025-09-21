package com.signlearn.persistence.repo;

import com.signlearn.domain.model.Video;
import java.util.Optional;

public interface VideoRepository {
    Optional<Video> findByLessonId(long lessonId);
    void insert(long lessonId, Video video);
    void update(long lessonId, Video video);
    void deleteByLessonId(long lessonId);
}