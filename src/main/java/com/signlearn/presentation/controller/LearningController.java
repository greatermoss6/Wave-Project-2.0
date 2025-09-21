package com.signlearn.presentation.controller;

import com.signlearn.app.context.LearningContext;
import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.model.Module;
import com.signlearn.domain.model.Chapter;
import com.signlearn.domain.model.Lesson;
import com.signlearn.domain.service.LearningService;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Controller for the learning page.
 * Displays modules, chapters, and lessons.
 */
public class LearningController extends BaseController implements DependencyAware {
    private SceneRouter router;
    private LearningService learningService;

    @FXML private ListView<Module> moduleList;
    @FXML private ListView<Chapter> chapterList;
    @FXML private ListView<Lesson> lessonList;
    @FXML private Button openLessonBtn;
    @FXML private Button logoutBtn;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.learningService = registry.getLearningService();
    }

    @Override
    public void postInit() {
        // Populate initial data
        List<Module> modules = learningService.listModules();
        moduleList.getItems().setAll(modules);

        moduleList.getSelectionModel().selectedItemProperty().addListener((obs, old, m) -> {
            if (m != null) {
                chapterList.getItems().setAll(learningService.listChapters(m.getId()));
            }
        });

        chapterList.getSelectionModel().selectedItemProperty().addListener((obs, old, c) -> {
            if (c != null) {
                lessonList.getItems().setAll(learningService.listLessons(c.getId()));
            }
        });

        openLessonBtn.setOnAction(e -> {
            Lesson selected = lessonList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                LearningContext.setCurrentLessonId(selected.getId());
                router.goTo(View.VIDEO);
            }
        });

        logoutBtn.setOnAction(e -> {
            LearningContext.clear();
            router.goTo(View.LANDING);
        });
    }
}