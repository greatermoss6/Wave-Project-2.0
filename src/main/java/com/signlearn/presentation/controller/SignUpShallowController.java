package com.signlearn.presentation.controller;

import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.value.Email;
import com.signlearn.app.context.SessionManager;
import com.signlearn.presentation.feedback.FeedbackHandler;
import com.signlearn.util.Result;
import com.signlearn.domain.enums.SignupStatus;
import com.signlearn.util.ValidationResult;
import com.signlearn.validation.EmailValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SignUpShallowController extends BaseController implements DependencyAware {

    private AuthService authService;
    private SessionManager session;
    private SceneRouter router;

    @FXML private Button continueBtn, backBtn;
    @FXML private TextField emailField;
    @FXML private Label messageLabel, loginLabel, logoLabel;

    private FeedbackHandler feedback;
    private final EmailValidator emailValidator = new EmailValidator();

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
        this.feedback = new FeedbackHandler(messageLabel);

        continueBtn.setOnAction(e -> handleShallowSignup());
        backBtn.setOnAction(e -> router.goTo(View.LANDING));
        loginLabel.setOnMouseClicked(e -> router.goTo(View.LOGIN));
        logoLabel.setOnMouseClicked(e -> router.goTo(View.LANDING));
    }

    private void handleShallowSignup() {
        String rawEmail = emailField.getText().trim();
        ValidationResult validation = emailValidator.validate(rawEmail);

        if (!validation.isSuccess()) {
            feedback.applyError(emailField, validation.getError());
            return;
        }

        Result<SignupStatus> result = authService.shallowSignUp(rawEmail);

        if (!result.isSuccess()) {
            feedback.applyError(emailField, result.getError());
            return;
        }

        handleSignupStatus(result.getValue(), rawEmail);
    }

    private void handleSignupStatus(SignupStatus status, String email) {
        switch (status) {
            case DUPLICATE_EMAIL ->
                    feedback.applyDuplicate(
                            emailField,
                            "Email already exists. Redirecting to login...",
                            1000,
                            () -> redirectToLoginWithPrefill(email)
                    );

            case SUCCESS ->
                    feedback.applySuccess(
                            emailField,
                            "Great job! Let's create your profile!",
                            500,
                            () -> saveEmailAndProceed(email)
                    );

            default ->
                    feedback.applyError(emailField, "Unexpected error. Please try again.");
        }
    }

    private void redirectToLoginWithPrefill(String email) {
        router.goTo(View.LOGIN, controller -> {
            if (controller instanceof LoginController lc) {
                lc.prefillEmail(email);
            }
        });
    }

    private void saveEmailAndProceed(String email) {
        session.setAttribute("pendingEmail", new Email(email));
        router.goTo(View.SIGNUP_DEEP);
    }
}
