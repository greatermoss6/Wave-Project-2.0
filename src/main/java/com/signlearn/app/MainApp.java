package com.signlearn.app;

import com.signlearn.app.context.SessionManager;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.presentation.ui.WindowManager;
import com.signlearn.domain.service.*;
import com.signlearn.domain.service.impl.*;
import com.signlearn.persistence.sqlite.*;
import com.signlearn.security.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Build services and router
        ServiceRegistry registry = buildServiceRegistry();
        new SceneRouter(stage, registry, View.LANDING);
        stage.setTitle("Wave");

        // 2. Hand off window behavior
        new SceneRouter(stage, registry, View.LANDING);
        stage.setTitle("Wave");
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

        // Initialize window manager
        WindowManager windowManager = new WindowManager(stage);

        // Maximize after first show
        Platform.runLater(windowManager::maximize);
    }

    private ServiceRegistry buildServiceRegistry() {
        SqliteUserRepository userRepo = new SqliteUserRepository();
        SqliteModuleRepository moduleRepo = new SqliteModuleRepository();
        SqliteChapterRepository chapterRepo = new SqliteChapterRepository();
        SqliteLessonRepository lessonRepo = new SqliteLessonRepository();
        SqliteQuestionRepository questionRepo = new SqliteQuestionRepository();
        SqliteProgressRepository progressRepo = new SqliteProgressRepository();
        SqliteVideoRepository videoRepo = new SqliteVideoRepository();

        SessionManager sessionManager = new SessionManager();
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        return new ServiceRegistry(
                new AuthServiceImpl(userRepo, passwordHasher, sessionManager),
                sessionManager,
                new LearningServiceImpl(moduleRepo, chapterRepo, lessonRepo),
                new QuestionServiceImpl(questionRepo),
                new ProgressServiceImpl(progressRepo, lessonRepo, chapterRepo, moduleRepo),
                new VideoServiceImpl(videoRepo),
                new UserServiceImpl(userRepo),
                passwordHasher
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}