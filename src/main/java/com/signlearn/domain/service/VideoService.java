package com.signlearn.domain.service;

import java.util.Optional;
import com.signlearn.domain.model.Video;

public interface VideoService {
    Optional<Video> getVideoForLesson(long lessonId);
}