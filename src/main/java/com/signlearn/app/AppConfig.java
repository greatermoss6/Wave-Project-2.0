package com.signlearn.app;

import com.signlearn.app.context.SessionManager;
import com.signlearn.domain.service.*;
import com.signlearn.domain.service.impl.*;
import com.signlearn.persistence.sqlite.*;
import com.signlearn.persistence.repo.*;
import com.signlearn.security.*;

// Central dependency wiring
public class AppConfig {
    private static final UserRepository userRepo = new SqliteUserRepository();
    private static final ModuleRepository moduleRepo = new SqliteModuleRepository();
    private static final ChapterRepository chapterRepo = new SqliteChapterRepository();
    private static final LessonRepository lessonRepo = new SqliteLessonRepository();
    private static final VideoRepository videoRepo = new SqliteVideoRepository();
    private static final QuestionRepository questionRepo = new SqliteQuestionRepository();
    private static final ProgressRepository progressRepo = new SqliteProgressRepository();

    private static final PasswordHasher hasher = new BCryptPasswordHasher();
    private static final SessionManager session = new SessionManager();

    // Services
    private static final AuthService authService =
            new AuthServiceImpl(userRepo, hasher, session);

    private static final LearningService learningService =
            new LearningServiceImpl(moduleRepo, chapterRepo, lessonRepo);

    private static final QuestionService questionService =
            new QuestionServiceImpl(questionRepo);

    private static final ProgressService progressService =
            new ProgressServiceImpl(progressRepo, lessonRepo, chapterRepo, moduleRepo);

    private static final VideoService videoService =
            new VideoServiceImpl(videoRepo);

    // === Getters ===
    public static AuthService getAuthService() { return authService; }
    public static LearningService getLearningService() { return learningService; }
    public static QuestionService getQuestionService() { return questionService; }
    public static ProgressService getProgressService() { return progressService; }
    public static VideoService getVideoService() { return videoService; }
    public static SessionManager getSessionManager() { return session; }
}