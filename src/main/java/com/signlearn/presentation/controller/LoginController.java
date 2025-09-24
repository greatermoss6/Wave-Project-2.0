package com.signlearn.presentation.controller;

import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.model.User;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.service.UserService;
import com.signlearn.domain.value.Email;
import com.signlearn.app.context.SessionManager;
import com.signlearn.util.Result;
import com.signlearn.util.Animations;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Controller for the Login view.
 * Handles user authentication and navigation.
 */
public class LoginController extends BaseController implements DependencyAware {
    private SceneRouter router;
    private AuthService authService;
    private UserService userService;
    private SessionManager session;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel, signupLabel, logoLabel;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.authService = registry.getAuthService();
        this.userService = registry.getUserService(); // needed to support username login without changing AuthService API
        this.session = registry.getSessionManager();
    }

    @Override
    public void postInit() {
        loginBtn.setOnAction(e -> handleLogin());
        backBtn.setOnAction(e -> router.goTo(View.LANDING));
        signupLabel.setOnMouseClicked(e -> router.goTo(View.SIGNUP_SHALLOW));
        logoLabel.setOnMouseClicked(e -> router.goTo(View.LANDING));
    }

    public void prefillEmail(String email) {
        emailField.setText(email);
    }

    private void handleLogin() {
        clearFeedback();

        final String principal = emailField.getText().trim(); // username OR email
        final String password  = passwordField.getText();

        if (principal.isEmpty() || password.isEmpty()) {
            showError("Please enter your username/email and password.");
            markError(emailField);
            markError(passwordField);
            Animations.shake(emailField);
            Animations.shake(passwordField);
            return;
        }

        Result<User> result;

        // If it looks like an email, use existing AuthService.login(Email, String)
        if (principal.contains("@")) {
            result = authService.login(new Email(principal), password);
        } else {
            // Username path without changing AuthService interface:
            // 1) Fetch user by username
            User u = userService.findByUsername(principal);
            if (u == null) {
                showError("No account found for that username or email.");
                markError(emailField);
                markError(passwordField);
                Animations.shake(emailField);
                Animations.shake(passwordField);
                return;
            }
            // 2) Delegate to existing email login to reuse hashing & session logic
            result = authService.login(u.getEmail(), password);
        }

        if (result.isSuccess()) {
            handleLoginSuccess(result.getValue());
        } else {
            handleLoginFailure(result.getError());
        }
    }

    private void handleLoginSuccess(User user) {
        session.set(user);
        showSuccess("Logging in…");

        markSuccess(emailField);
        markSuccess(passwordField);

        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> router.goTo(View.LEARNING));
        pause.play();
    }

    private void handleLoginFailure(String error) {
        showError(error != null ? error : "Invalid credentials.");
        markError(emailField);
        markError(passwordField);
        Animations.shake(emailField);
        Animations.shake(passwordField);
    }

    // --- UI feedback helpers (kept local to avoid changing shared classes) ---

    private void clearFeedback() {
        emailField.setStyle("");
        passwordField.setStyle("");
        messageLabel.setText("");
        messageLabel.setVisible(false);
        messageLabel.setManaged(false);
    }

    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(Color.web("#ef4444")); // red
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    private void showSuccess(String msg) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(Color.web("#16a34a")); // green
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    private void markError(Control c) {
        c.setStyle("-fx-border-color: #ef4444; -fx-border-width: 2;");
    }

    private void markSuccess(Control c) {
        c.setStyle("-fx-border-color: #16a34a; -fx-border-width: 2;");
    }
}