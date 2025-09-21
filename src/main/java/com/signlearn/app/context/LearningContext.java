package com.signlearn.app.context;

/**
 * Lightweight shared state to track which lesson is currently active.
 * Controllers use this instead of hardcoding lessonId.
 */
public class LearningContext {
    private static Long currentLessonId;

    public static void setCurrentLessonId(Long id) {
        currentLessonId = id;
    }

    public static Long getCurrentLessonId() {
        return currentLessonId;
    }

    public static void clear() {
        currentLessonId = null;
    }
}