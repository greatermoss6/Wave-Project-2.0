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
import com.signlearn.validation.SignUpValidator;
import com.signlearn.util.Result;
import com.signlearn.util.ValidationResult;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDate;

public class SignUpDeepController extends BaseController implements DependencyAware {

    private AuthService authService;
    private UserService userService;
    private SessionManager session;
    private SceneRouter router;
    private SignUpValidator validator;

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private DatePicker dobPicker;
    @FXML private Button finishBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

    private final PauseTransition usernameDebounce = new PauseTransition(Duration.millis(300));

    @Override
    public void setRouter(SceneRouter router) { this.router = router; }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.authService = registry.getAuthService();
        this.userService = registry.getUserService();
        this.session = registry.getSessionManager();
        this.validator = new SignUpValidator(registry.getPasswordHasher());
    }

    @Override
    public void postInit() {
        // Debounced username availability check
        usernameField.textProperty().addListener((obs, oldText, newText) -> {
            usernameDebounce.stop();
            usernameDebounce.setOnFinished(ev -> checkUsernameAvailability(newText));
            usernameDebounce.playFromStart();
        });

        finishBtn.setOnAction(e -> handleDeepSignup());
        backBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
    }

    private void checkUsernameAvailability(String username) {
        // Empty/invalid format handled by validator later; only mark available if format is okay
        boolean formatOk = username != null && username.matches("^[A-Za-z0-9_]{3,20}$");
        if (!formatOk) {
            markInvalid(usernameField, "Username format: 3–20 letters/numbers/underscore.");
            return;
        }
        boolean available = userService.isUsernameAvailable(username);
        if (available) {
            markValid(usernameField, "Username is available ✓");
        } else {
            markInvalid(usernameField, "Username is taken.");
        }
    }

    private void handleDeepSignup() {
        Email email = getPendingEmail();
        if (email == null) {
            showErrorAndRedirect("No pending email. Please start signup again.", View.SIGNUP_SHALLOW);
            return;
        }

        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String rawPassword = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        LocalDate dob = dobPicker.getValue();

        // Cross-field validation
        ValidationResult vr = validator.validate(name, username, rawPassword, confirmPassword, dob);
        if (!vr.isValid()) {
            messageLabel.setText(vr.getErrorMessage());
            return;
        }

        // Check availability again to avoid race (UI is best-effort)
        if (!userService.isUsernameAvailable(username)) {
            messageLabel.setText("Username is already taken.");
            markInvalid(usernameField, null);
            return;
        }

        // Build user (service will hash password and set createdAt)
        User newUser = new User(email, null, name, username, dob);
        Result<User> result = authService.deepSignUpWithPassword(newUser, rawPassword);

        if (result.isSuccess()) {
            session.removeAttribute("pendingEmail");
            router.goTo(View.LEARNING);
        } else {
            messageLabel.setText(result.getError());
        }
    }

    private Email getPendingEmail() { return (Email) session.getAttribute("pendingEmail"); }

    private void showErrorAndRedirect(String message, View view) {
        messageLabel.setText(message);
        router.goTo(view);
    }

    private void markValid(TextField field, String msg) {
        field.getStyleClass().removeAll("invalid-field");
        if (!field.getStyleClass().contains("valid-field")) field.getStyleClass().add("valid-field");
        if (msg != null) messageLabel.setText(msg);
    }

    private void markInvalid(TextField field, String msg) {
        field.getStyleClass().removeAll("valid-field");
        if (!field.getStyleClass().contains("invalid-field")) field.getStyleClass().add("invalid-field");
        if (msg != null) messageLabel.setText(msg);
    }
}