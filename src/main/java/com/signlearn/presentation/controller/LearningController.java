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

    @FXML private ComboBox<Module> moduleComboBox;
    @FXML private ComboBox<Chapter> chapterComboBox;
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
        moduleComboBox.getItems().setAll(modules);

        // Set up module selection listener
        moduleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldModule, newModule) -> {
            if (newModule != null) {
                // Load chapters for the selected module
                List<Chapter> chapters = learningService.listChapters(newModule.getId());
                chapterComboBox.getItems().setAll(chapters);
                chapterComboBox.getSelectionModel().clearSelection(); // Clear previous chapter selection
                chapterComboBox.setDisable(false); // Enable chapter dropdown
            } else {
                // Clear chapters if no module selected
                chapterComboBox.getItems().clear();
                chapterComboBox.setDisable(true);
            }
        });

        // Set up chapter selection listener
        chapterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldChapter, newChapter) -> {
            // Enable the start lesson button when a chapter is selected
            openLessonBtn.setDisable(newChapter == null);
        });

        // Initially disable chapter selection and start lesson button
        chapterComboBox.setDisable(true);
        openLessonBtn.setDisable(true);

        // Set up start lesson button action
        openLessonBtn.setOnAction(e -> {
            Chapter selectedChapter = chapterComboBox.getSelectionModel().getSelectedItem();
            if (selectedChapter != null) {
                // Get the first lesson from the selected chapter
                List<Lesson> lessons = learningService.listLessons(selectedChapter.getId());
                if (!lessons.isEmpty()) {
                    Lesson firstLesson = lessons.get(0); // Start with first lesson
                    LearningContext.setCurrentLessonId(firstLesson.getId());
                    router.goTo(View.VIDEO);
                } else {
                    // Handle case where chapter has no lessons
                    showAlert("No Lessons", "This chapter doesn't contain any lessons yet.");
                }
            }
        });

        logoutBtn.setOnAction(e -> {
            LearningContext.clear();
            router.goTo(View.LANDING);
        });
    }
}