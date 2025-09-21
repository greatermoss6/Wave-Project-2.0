package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.Progress;
import com.signlearn.domain.model.Lesson;
import com.signlearn.domain.model.Chapter;
import com.signlearn.domain.model.Module;
import com.signlearn.domain.service.ProgressService;
import com.signlearn.persistence.repo.*;

import java.util.*;
import java.util.stream.Collectors;

public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepo;
    private final LessonRepository lessonRepo;
    private final ChapterRepository chapterRepo;
    private final ModuleRepository moduleRepo;

    public ProgressServiceImpl(ProgressRepository progressRepo,
                               LessonRepository lessonRepo,
                               ChapterRepository chapterRepo,
                               ModuleRepository moduleRepo) {
        this.progressRepo = progressRepo;
        this.lessonRepo = lessonRepo;
        this.chapterRepo = chapterRepo;
        this.moduleRepo = moduleRepo;
    }

    @Override
    public void recordAttempt(long userId, long lessonId, int scorePercent) {
        Progress progress = new Progress(userId, lessonId, false, scorePercent);
        progressRepo.upsert(progress);
    }

    @Override
    public void markCompleted(long userId, long lessonId) {
        Optional<Progress> maybe = progressRepo.find(userId, lessonId);
        Progress updated = maybe
                .map(p -> new Progress(p.getUserId(), p.getLessonId(), true, p.getScorePercent()))
                .orElse(new Progress(userId, lessonId, true, 0));
        progressRepo.upsert(updated);
    }

    @Override
    public Optional<Progress> getProgress(long userId, long lessonId) {
        return progressRepo.find(userId, lessonId);
    }

    @Override
    public Map<Long, Double> summarizeByModule(long userId) {
        List<Progress> progressList = progressRepo.findByUser(userId);

        // Group progress by module
        return progressList.stream().collect(Collectors.groupingBy(
                p -> {
                    Lesson lesson = lessonRepo.findById(p.getLessonId()).orElse(null);
                    if (lesson == null) return -1L;

                    Chapter chapter = chapterRepo.findById(lesson.getChapterId()).orElse(null);
                    if (chapter == null) return -1L;

                    Module module = moduleRepo.findById(chapter.getModuleId()).orElse(null);
                    return (module != null) ? module.getId() : -1L;
                },
                Collectors.averagingInt(Progress::getScorePercent)
        ));
    }
}