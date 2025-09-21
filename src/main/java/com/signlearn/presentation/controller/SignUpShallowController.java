package com.signlearn.presentation.controller;

import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.value.Email;
import com.signlearn.app.context.SessionManager;
import com.signlearn.util.Result;
import com.signlearn.util.SignupStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the shallow signup step (email only).
 * Refactored for clarity and cohesion.
 */
public class SignUpShallowController extends BaseController implements DependencyAware {

    private AuthService authService;
    private SessionManager session;
    private SceneRouter router;

    @FXML private TextField emailField;
    @FXML private Button continueBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

    private static final java.util.regex.Pattern EMAIL =
            java.util.regex.Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private boolean looksLikeEmail(String e) {
        return e != null && EMAIL.matcher(e).matches();
    }

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
        continueBtn.setOnAction(e -> handleShallowSignup());
        backBtn.setOnAction(e -> router.goTo(View.LANDING));
    }

    /**
     * Orchestrates shallow signup: validate input, call service, and handle result.
     */
    private void handleShallowSignup() {
        String email = emailField.getText().trim();

        if (!looksLikeEmail(email)) {
            messageLabel.setText("Please enter a valid email address.");
            return;
        }

        Result<SignupStatus> result = authService.shallowSignUp(email);

        if (!result.isSuccess()) {
            showMessage(result.getError());
            return;
        }

        handleSignupStatus(result.getValue(), email);
    }

    /**
     * Reads the email input field.
     */
    private String getEmailInput() {
        return emailField.getText().trim();
    }

    /**
     * Handles each possible signup outcome.
     */
    private void handleSignupStatus(SignupStatus status, String email) {
        switch (status) {
            case DUPLICATE_EMAIL -> redirectToLoginWithPrefill(email);
            case SUCCESS -> saveEmailAndProceed(email);
            default -> showMessage("Something went wrong. Please try again.");
        }
    }

    /**
     * Redirects to login with the email pre-filled.
     */
    private void redirectToLoginWithPrefill(String email) {
        router.goTo(View.LOGIN, controller -> {
            if (controller instanceof LoginController lc) {
                lc.prefillEmail(email);
            }
        });
    }

    /**
     * Saves email for deep signup and goes to the next step.
     */
    private void saveEmailAndProceed(String email) {
        session.setAttribute("pendingEmail", new Email(email));
        router.goTo(View.SIGNUP_DEEP);
    }

    /**
     * Displays a message in the UI.
     */
    private void showMessage(String msg) {
        messageLabel.setText(msg);
    }
}