package com.signlearn.ui;

import com.signlearn.app.MainApp;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxToolkit.registerPrimaryStage;

public class LandingPageTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new MainApp().start(stage);
    }

    /**
     * 3a: Given I am on the landing page,
     *     when I view the page,
     *     then a Login button is visible in the top-right area.
     */
    @Test
    void testLoginButtonVisible() {
        // loginBtn must have fx:id="loginBtn" in landing.fxml
        assertTrue(lookup("#loginBtn").tryQuery().isPresent(),
                "Login button should be visible on landing page");
    }

    /**
     * 3b: Given I am on the landing page,
     *     when I click the Login button,
     *     then I am redirected to the login page.
     */
    @Test
    void testLoginButtonRedirectsToLoginPage() {
        clickOn("#loginBtn");

        // Check login page loaded by looking for email field
        assertTrue(lookup("#emailField").tryQuery().isPresent(),
                "Clicking Login should redirect to login page with email field");
    }
}