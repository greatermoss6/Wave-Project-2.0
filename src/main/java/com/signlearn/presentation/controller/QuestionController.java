package com.signlearn.presentation.controller;

import com.signlearn.app.context.LearningContext;
import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.model.MultipleChoiceQuestion;
import com.signlearn.domain.model.Question;
import com.signlearn.domain.model.User;
import com.signlearn.domain.service.ProgressService;
import com.signlearn.domain.service.QuestionService;
import com.signlearn.app.context.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

/**
 * Controller for displaying and handling lesson questions.
 * Refactored for clarity and cohesion.
 */
public class QuestionController extends BaseController implements DependencyAware {
    private SceneRouter router;
    private QuestionService questionService;
    private ProgressService progressService;
    private SessionManager session;

    @FXML private Label promptLabel;
    @FXML private VBox optionsBox;
    @FXML private Button submitBtn;
    @FXML private Button backBtn;
    @FXML private Label feedbackLabel;

    private List<Question> questions;
    private int currentIndex = 0;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.questionService = registry.getQuestionService();
        this.progressService = registry.getProgressService();
        this.session = registry.getSessionManager();
    }

    @Override
    public void postInit() {
        long lessonId = LearningContext.getCurrentLessonId();
        questions = questionService.questionsForLesson(lessonId);

        if (!questions.isEmpty()) {
            loadQuestion(questions.get(currentIndex));
        }

        submitBtn.setOnAction(e -> handleSubmit());
        backBtn.setOnAction(e -> router.goTo(View.LEARNING));
    }

    private void loadQuestion(Question q) {
        promptLabel.setText(q.getPrompt());
        optionsBox.getChildren().clear();

        if (q instanceof MultipleChoiceQuestion mcq) {
            ToggleGroup group = new ToggleGroup();
            for (int i = 0; i < mcq.getOptions().size(); i++) {
                RadioButton rb = new RadioButton(mcq.getOptions().get(i).getText());
                rb.setUserData(i);
                rb.setToggleGroup(group);
                optionsBox.getChildren().add(rb);
            }
            optionsBox.setUserData(group);
        } else {
            feedbackLabel.setText("Unsupported question type: " + q.type());
        }
    }

    /**
     * Handles the submission of the current question.
     */
    private void handleSubmit() {
        if (questions.isEmpty()) return;

        Question q = questions.get(currentIndex);
        if (!(q instanceof MultipleChoiceQuestion)) {
            feedbackLabel.setText("Unsupported question type.");
            return;
        }

        Optional<Integer> maybeSelection = getSelectedOption();
        if (maybeSelection.isEmpty()) {
            feedbackLabel.setText("Please select an answer.");
            return;
        }

        User current = session.get();
        if (current == null) {
            feedbackLabel.setText("Not logged in.");
            return;
        }

        int selectedIndex = maybeSelection.get();
        boolean correct = questionService.check(q, selectedIndex);

        recordProgress(current, q, correct);
        updateFeedback(correct, current, q);
    }

    /**
     * Retrieves the selected option index from the UI.
     */
    private Optional<Integer> getSelectedOption() {
        ToggleGroup group = (ToggleGroup) optionsBox.getUserData();
        if (group == null || group.getSelectedToggle() == null) {
            return Optional.empty();
        }
        return Optional.of((int) group.getSelectedToggle().getUserData());
    }

    /**
     * Records the user's attempt and progress in the service layer.
     */
    private void recordProgress(User user, Question q, boolean correct) {
        int score = correct ? 100 : 0;
        progressService.recordAttempt(user.getId(), q.getLessonId(), score);
        if (correct) {
            progressService.markCompleted(user.getId(), q.getLessonId());
        }
    }

    /**
     * Updates the feedback label depending on the result.
     */
    private void updateFeedback(boolean correct, User user, Question q) {
        if (correct) {
            feedbackLabel.setText("Correct! Lesson completed.");
        } else {
            feedbackLabel.setText("Try again.");
        }
    }
}