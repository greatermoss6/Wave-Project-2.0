package com.signlearn.domain.service;

import com.signlearn.domain.model.Video;
import java.util.Optional;

public interface VideoService {
    Optional<Video> getVideoForLesson(long lessonId);
}