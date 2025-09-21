package com.signlearn.domain.service;

import com.signlearn.domain.model.Progress;

import java.util.Map;
import java.util.Optional;

public interface ProgressService {
    /**
     * Records an attempt for a given lesson.
     * Does not automatically mark the lesson as completed.
     */
    void recordAttempt(long userId, long lessonId, int scorePercent);

    /**
     * Explicitly mark a lesson as completed (e.g., after passing quiz).
     */
    void markCompleted(long userId, long lessonId);

    /**
     * Retrieve detailed progress for a specific lesson.
     */
    Optional<Progress> getProgress(long userId, long lessonId);

    /**
     * Summarize progress by module.
     * Returns a map of moduleId → average score across its lessons.
     */
    Map<Long, Double> summarizeByModule(long userId);
}