package com.signlearn.app.dependency_injection;

import com.signlearn.domain.service.*;
import com.signlearn.app.context.SessionManager;
import com.signlearn.security.PasswordHasher;

/**
 * Central holder for application services.
 * Keeps things encapsulated so controllers only get what they need.
 */
public class ServiceRegistry {
    private final AuthService authService;
    private final SessionManager sessionManager;
    private final LearningService learningService;
    private final QuestionService questionService;
    private final ProgressService progressService;
    private final VideoService videoService;
    private final UserService userService;
    private final PasswordHasher passwordHasher; // <-- NEW

    public ServiceRegistry(
            AuthService authService,
            SessionManager sessionManager,
            LearningService learningService,
            QuestionService questionService,
            ProgressService progressService,
            VideoService videoService,
            UserService userService,
            PasswordHasher passwordHasher // <-- NEW
    ) {
        this.authService = authService;
        this.sessionManager = sessionManager;
        this.learningService = learningService;
        this.questionService = questionService;
        this.progressService = progressService;
        this.videoService = videoService;
        this.userService = userService;
        this.passwordHasher = passwordHasher;
    }

    // Getters
    public AuthService getAuthService() { return authService; }
    public SessionManager getSessionManager() { return sessionManager; }
    public LearningService getLearningService() { return learningService; }
    public QuestionService getQuestionService() { return questionService; }
    public ProgressService getProgressService() { return progressService; }
    public VideoService getVideoService() { return videoService; }
    public UserService getUserService() { return userService; }
    public PasswordHasher getPasswordHasher() { return passwordHasher; } // <-- NEW
}
