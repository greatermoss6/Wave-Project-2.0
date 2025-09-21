package com.signlearn.app;

import com.signlearn.app.context.SessionManager;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.domain.service.*;
import com.signlearn.domain.service.impl.*;
import com.signlearn.persistence.sqlite.*;
import com.signlearn.security.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        // --- Repositories (concrete SQLite implementations) ---
        SqliteUserRepository userRepo = new SqliteUserRepository();
        SqliteModuleRepository moduleRepo = new SqliteModuleRepository();
        SqliteChapterRepository chapterRepo = new SqliteChapterRepository();
        SqliteLessonRepository lessonRepo = new SqliteLessonRepository();
        SqliteQuestionRepository questionRepo = new SqliteQuestionRepository();
        SqliteProgressRepository progressRepo = new SqliteProgressRepository();
        SqliteVideoRepository videoRepo = new SqliteVideoRepository();

        // --- Utilities ---
        SessionManager sessionManager = new SessionManager();
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        // --- Services ---
        UserService userService = new UserServiceImpl(userRepo);
        LearningService learningService = new LearningServiceImpl(moduleRepo, chapterRepo, lessonRepo);
        QuestionService questionService = new QuestionServiceImpl(questionRepo);
        ProgressService progressService = new ProgressServiceImpl(progressRepo, lessonRepo, chapterRepo, moduleRepo);
        AuthService authService = new AuthServiceImpl(userRepo, passwordHasher, sessionManager);
        VideoService videoService = new VideoServiceImpl(videoRepo);

        // --- Build the service registry (note the added passwordHasher arg) ---
        ServiceRegistry registry = new ServiceRegistry(
                authService,
                sessionManager,
                learningService,
                questionService,
                progressService,
                videoService,
                userService,
                passwordHasher
        );

        // Router loads initial view and shows stage
        new SceneRouter(stage, registry, View.LANDING);

        stage.setTitle("Wave");
        stage.setWidth(javafx.stage.Screen.getPrimary().getBounds().getWidth());
        stage.setHeight(javafx.stage.Screen.getPrimary().getBounds().getHeight());
        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
