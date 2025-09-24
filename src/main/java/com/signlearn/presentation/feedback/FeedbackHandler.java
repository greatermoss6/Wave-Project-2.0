package com.signlearn.presentation.feedback;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.signlearn.validation.PasswordValidator;
import com.signlearn.validation.UsernameValidator;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.List;

/**
 * Handles all presentation feedback for forms.
 * Keeps controllers thin by centralizing styling, messages, and rule/suggestion rendering.
 */
public class FeedbackHandler {
    private final Label messageLabel;

    public FeedbackHandler(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    // --- Generic error/success feedback ---
    public void applyError(Control field, String msg) {
        resetFieldClasses(field);
        field.getStyleClass().add("field-error");
        showMessage(msg, "#ef4444");
    }

    public void applyDuplicate(Control field, String msg) {
        resetFieldClasses(field);
        field.getStyleClass().add("field-duplicate");
        showMessage(msg, "#111184");
    }

    public void applySuccess(Control field, String msg) {
        resetFieldClasses(field);
        field.getStyleClass().add("field-success");
        showMessage(msg, "#16a34a");
    }

    public void applySuccess(Control field, String msg, int delayMillis, Runnable afterDelay) {
        applySuccess(field, msg);
        runAfterDelay(delayMillis, afterDelay);
    }

    public void applyDuplicate(Control field, String msg, int delayMillis, Runnable afterDelay) {
        applyDuplicate(field, msg);
        runAfterDelay(delayMillis, afterDelay);
    }

    private void runAfterDelay(int millis, Runnable action) {
        PauseTransition pause = new PauseTransition(Duration.millis(millis));
        pause.setOnFinished(e -> action.run());
        pause.play();
    }

    public void clear(Control field) {
        resetFieldClasses(field);
        hideMessage();
    }

    // --- Password rules ---
    public void showPasswordRules(VBox box, List<PasswordValidator.Rule> rules) {
        box.getChildren().clear();
        for (PasswordValidator.Rule r : rules) {
            Label l = new Label((r.isSatisfied() ? "✓ " : "• ") + r.getDescription());
            l.setStyle(r.isSatisfied()
                    ? "-fx-text-fill: #16a34a; -fx-font-size: 12px;"
                    : "-fx-text-fill: #ef4444; -fx-font-size: 12px;");
            box.getChildren().add(l);
        }
        box.setVisible(true);
        box.setManaged(true);
    }

    public void hidePasswordRules(VBox box) {
        box.setVisible(false);
        box.setManaged(false);
    }

    // --- Confirm password match rule ---
    public void showConfirmPasswordRule(VBox box, boolean match) {
        box.getChildren().clear();
        Label l = new Label(match ? "✓ Passwords match" : "• Passwords do not match");
        l.setStyle(match
                ? "-fx-text-fill: #16a34a; -fx-font-size: 12px;"
                : "-fx-text-fill: #ef4444; -fx-font-size: 12px;");
        box.getChildren().add(l);
        box.setVisible(true);
        box.setManaged(true);
    }

    public void hideConfirmPasswordRules(VBox box) {
        box.setVisible(false);
        box.setManaged(false);
    }

    public void updatePasswordAndConfirmRules(
            VBox passwordRulesBox,
            List<PasswordValidator.Rule> rules,
            VBox confirmBox,
            boolean match,
            boolean active
    ) {
        if (active) {
            // Show both together
            showPasswordRules(passwordRulesBox, rules);
            showConfirmPasswordRule(confirmBox, match);
        } else {
            // Hide both together
            hidePasswordRules(passwordRulesBox);
            hideConfirmPasswordRules(confirmBox);
        }
    }

    // --- Username suggestions ---
    public void showUsernameSuggestions(VBox box, UsernameValidator.UsernameValidationResult result) {
        box.getChildren().clear();

        if (result.hasSuggestions()) {
            for (String s : result.getSuggestions()) {
                Label suggestion = new Label(s);
                suggestion.getStyleClass().add("suggestion-label");
                box.getChildren().add(suggestion);
            }
            box.setVisible(true);
            box.setManaged(true);
        } else {
            hideUsernameSuggestions(box);
        }
    }

    public void hideUsernameSuggestions(VBox box) {
        box.getChildren().clear();
        box.setVisible(false);
        box.setManaged(false);
    }

    // --- Private helpers ---
    private void showMessage(String msg, String color) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    private void hideMessage() {
        messageLabel.setVisible(false);
        messageLabel.setManaged(false);
    }

    private void resetFieldClasses(Control field) {
        field.getStyleClass().removeAll("field-error", "field-success", "field-duplicate");
    }

    public void hideNameRules(VBox box) {
        box.setVisible(false);
        box.setManaged(false);
        box.getChildren().clear();
    }
}