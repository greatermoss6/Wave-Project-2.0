package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.Video;
import com.signlearn.domain.service.VideoService;
import com.signlearn.persistence.repo.VideoRepository;

import java.util.Optional;

public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepo;

    public VideoServiceImpl(VideoRepository videoRepo) {
        this.videoRepo = videoRepo;
    }

    @Override
    public Optional<Video> getVideoForLesson(long lessonId) {
        return videoRepo.findByLessonId(lessonId);
    }
}