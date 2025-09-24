package com.signlearn.presentation.controller;

import com.signlearn.app.context.SessionManager;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.domain.enums.Gender;
import com.signlearn.domain.model.User;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.service.UserService;
import com.signlearn.domain.value.Email;
import com.signlearn.presentation.feedback.FeedbackHandler;
import com.signlearn.security.PasswordHasher;
import com.signlearn.util.Result;
import com.signlearn.domain.enums.SignupStatus;
import com.signlearn.validation.PasswordValidator;
import com.signlearn.validation.UsernameValidator;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class SignUpDeepController extends BaseController implements DependencyAware {

    private AuthService authService;
    private UserService userService;
    private SessionManager session;
    private SceneRouter router;
    private FeedbackHandler feedback;
    private PasswordHasher passwordHasher;
    private UsernameValidator usernameValidator;

    @FXML private TextField usernameField, nameField;
    @FXML private VBox usernameSuggestionsBox;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private VBox passwordRulesBox;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderCombo;
    @FXML private Button finishBtn, backBtn;
    @FXML private Label messageLabel, logoLabel;
    @FXML private VBox confirmPasswordRuleBox;
    @FXML private VBox nameRuleBox;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.authService     = registry.getAuthService();
        this.userService     = registry.getUserService();
        this.session         = registry.getSessionManager();
        this.passwordHasher  = registry.getPasswordHasher();
        this.usernameValidator = new UsernameValidator(userService);
    }

    @Override
    public void postInit() {
        this.feedback = new FeedbackHandler(messageLabel);

        finishBtn.setOnAction(e -> handleDeepSignup());
        backBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        logoLabel.setOnMouseClicked(e -> router.goTo(View.LANDING));

        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {handleUsernameInput();});
        passwordField.textProperty().addListener((obs, oldV, newV) -> updatePasswordAndConfirmRules());
        confirmPasswordField.textProperty().addListener((obs, oldV, newV) -> updatePasswordAndConfirmRules());

        nameField.textProperty().addListener((obs, oldV, newV) -> updateNameRules(newV));

        // populate genders
        genderCombo.getItems().setAll("Male", "Female", "Non-binary", "Prefer not to say");
    }

    private void handleUsernameInput() {
        String username = usernameField.getText().trim();
        usernameSuggestionsBox.getChildren().clear();

        if (username.isEmpty()) {
            feedback.clear(usernameField);
            syncNameUsernameBoxes(!nameRuleBox.getChildren().isEmpty());
            return;
        }

        UsernameValidator.UsernameValidationResult res =
                usernameValidator.validateWithSuggestions(username, 3);

        if (!res.getResult().isSuccess()) {
            feedback.applyError(usernameField, res.getResult().getMessage());
            for (String s : res.getSuggestions()) {
                Label suggestion = new Label(s);
                suggestion.getStyleClass().add("suggestion-label");
                suggestion.setOnMouseClicked(e -> {
                    usernameField.setText(s);
                    feedback.applySuccess(usernameField, "Username is available");
                    usernameSuggestionsBox.getChildren().clear();
                    syncNameUsernameBoxes(!nameRuleBox.getChildren().isEmpty());
                });
                usernameSuggestionsBox.getChildren().add(suggestion);
            }
            syncNameUsernameBoxes(true);
        } else {
            feedback.applySuccess(usernameField, "Username is available");
            syncNameUsernameBoxes(!nameRuleBox.getChildren().isEmpty());
        }
    }

    private void updateNameRules(String fullName) {
        nameRuleBox.getChildren().clear();

        boolean valid = fullName != null && fullName.trim().contains(" ");
        Label rule = new Label();

        if (fullName == null || fullName.isBlank()) {
            rule.setText("• Please enter both first and last name");
            rule.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px;");
            nameRuleBox.getChildren().add(rule);
            syncNameUsernameBoxes(true);
            return;
        }

        if (!valid) {
            rule.setText("• Please enter both first and last name");
            rule.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px;");
        } else {
            rule.setText("✓ Looks good");
            rule.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 12px;");
        }

        nameRuleBox.getChildren().add(rule);
        syncNameUsernameBoxes(true);
    }

    private void syncNameUsernameBoxes(boolean show) {
        nameRuleBox.setVisible(show);
        nameRuleBox.setManaged(show);
        usernameSuggestionsBox.setVisible(show);
        usernameSuggestionsBox.setManaged(show);
    }

    private void updatePasswordAndConfirmRules() {
        String password = passwordField.getText();
        String confirm  = confirmPasswordField.getText();

        var rules = PasswordValidator.evaluate(password);

        boolean active = !(password.isEmpty() && confirm.isEmpty());

        if (active) {
            feedback.updatePasswordAndConfirmRules(
                    passwordRulesBox,
                    rules,
                    confirmPasswordRuleBox,
                    confirm.equals(password),
                    true
            );
        } else {
            feedback.updatePasswordAndConfirmRules(
                    passwordRulesBox,
                    rules,
                    confirmPasswordRuleBox,
                    false,
                    false
            );
        }
    }

    private void handleDeepSignup() {
        String name           = nameField.getText().trim();
        String username       = usernameField.getText().trim();
        String password       = passwordField.getText();
        String confirmPassword= confirmPasswordField.getText();
        LocalDate dob         = dobPicker.getValue();
        String genderStr      = genderCombo.getValue();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || dob == null || genderStr == null) {
            feedback.applyError(messageLabel, "Please fill all required fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            feedback.applyError(messageLabel, "Passwords do not match");
            return;
        }

        Object o = session.getAttribute("pendingEmail");
        Email pendingEmail = (o instanceof Email e) ? e : null;
        if (pendingEmail == null) {
            feedback.applyError(messageLabel, "Missing email from previous step. Please start again.");
            router.goTo(View.SIGNUP_SHALLOW);
            return;
        }

        String passwordHash = passwordHasher.hash(password);
        Gender gender = Gender.fromDisplayName(genderStr);

        User newUser = new User(
                pendingEmail,
                passwordHash,
                name,
                username,
                dob,
                gender
        );

        Result<SignupStatus> result = authService.deepSignUp(newUser);

        if (!result.isSuccess()) {
            feedback.applyError(messageLabel, result.getError());
            return;
        }

        feedback.applySuccess(messageLabel, "Signup successful!");
        router.goTo(View.LEARNING);
    }
}
