package com.signlearn.presentation.controller;

import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.model.User;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.value.Email;
import com.signlearn.app.context.SessionManager;
import com.signlearn.util.Result;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the Login view.
 * Handles user authentication and navigation.
 */
public class LoginController extends BaseController implements DependencyAware {
    private SceneRouter router;
    private AuthService authService;
    private SessionManager session;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.authService = registry.getAuthService();
        this.session = registry.getSessionManager();
    }

    @Override
    public void postInit() {
        loginBtn.setOnAction(e -> handleLogin());
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        backBtn.setOnAction(e -> router.goTo(View.LANDING));
    }

    /**
     * Prefills the email field, used when redirecting from signup.
     */
    public void prefillEmail(String email) {
        emailField.setText(email);
    }

    /**
     * Orchestrates login: gather input, call service, handle result.
     */
    private void handleLogin() {
        String email = getEmailInput();
        String password = getPasswordInput();

        Result<User> result = authService.login(new Email(email), password);
        if (result.isSuccess()) {
            handleLoginSuccess(result.getValue());
        } else {
            handleLoginFailure(result.getError());
        }
    }

    /**
     * Reads the email input field.
     */
    private String getEmailInput() {
        return emailField.getText().trim();
    }

    /**
     * Reads the password input field.
     */
    private String getPasswordInput() {
        return passwordField.getText();
    }

    /**
     * Handles login success: greet user, set session, navigate.
     */
    private void handleLoginSuccess(User user) {
        messageLabel.setText("Welcome, " + user.getName());
        session.set(user);
        router.goTo(View.LEARNING);
    }

    /**
     * Handles login failure: show error message.
     */
    private void handleLoginFailure(String error) {
        messageLabel.setText(error);
    }
}