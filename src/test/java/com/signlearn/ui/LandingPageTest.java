package com.signlearn.ui;

import com.signlearn.app.MainApp;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LandingPageTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new MainApp().start(stage);
    }

    /**
     * 1a: Given I am on the landing page,
     *     when I view the page,
     *     then a Login button is visible in the top-right area.
     */
    @Test
    void testLoginButtonVisible() {
        assertTrue(lookup("#loginBtn").tryQuery().isPresent(),
                "Login button should be visible on landing page");
    }

    /**
     * 1b: Given I am on the landing page,
     *     when I click the Login button,
     *     then I am redirected to the login page.
     */
    @Test
    void testLoginButtonRedirectsToLoginPage() {
        clickOn("#loginBtn");
        assertTrue(lookup("#emailField").tryQuery().isPresent(),
                "Clicking Login should redirect to login page with email field");
    }

    /**
     * 2a: Given I am on the landing page,
     *     when I view the window,
     *     then the application title is visible.
     */
    @Test
    void testAppTitleVisible() {
        assertTrue(lookup("#appTitleLabel").tryQuery().isPresent(),
                "App title should be visible on landing page");
    }

    /**
     * 2b: Given I am on the landing page,
     *     when I view the page,
     *     then a Sign Up button is visible.
     */
    @Test
    void testSignUpButtonVisible() {
        assertTrue(lookup("#signupBtn").tryQuery().isPresent(),
                "Sign Up button should be visible on landing page");
    }

    /**
     * 3a: Given I am on the landing page,
     *     when I click the Sign Up button,
     *     then I am redirected to the sign up page.
     */
    @Test
    void testSignUpButtonRedirectsToSignUpPage() {
        clickOn("#signupBtn");
        assertTrue(lookup("#signupForm").tryQuery().isPresent(),
                "Clicking Sign Up should redirect to the sign up page");
    }

    /**
     * 3b: Given I am on the landing page,
     *     when I view the page,
     *     then the hero image or banner is visible.
     */
    @Test
    void testHeroImageVisible() {
        assertTrue(lookup("#heroImage").tryQuery().isPresent(),
                "Hero image should be visible on landing page");
    }

    /**
     * 4a: Given I am on the landing page,
     *     when I view the header,
     *     then a navigation menu is present.
     */
    @Test
    void testNavigationMenuVisible() {
        assertTrue(lookup("#navMenu").tryQuery().isPresent(),
                "Navigation menu should be visible on landing page");
    }

    /**
     * 4b: Given I navigate away from landing page,
     *     when I click the logo,
     *     then I am redirected back to the landing page.
     */
    @Test
    void testLogoRedirectsToLandingPage() {
        clickOn("#loginBtn"); // Navigate away
        clickOn("#logoImage"); // Click logo
        assertTrue(lookup("#loginBtn").tryQuery().isPresent(),
                "Clicking logo should return to landing page");
    }

    /**
     * 5a: Given I am on the landing page,
     *     when I view the footer,
     *     then a Terms & Conditions link is visible.
     */
    @Test
    void testTermsAndConditionsVisible() {
        assertTrue(lookup("#termsLink").tryQuery().isPresent(),
                "Terms & Conditions link should be visible in footer");
    }

    /**
     * 5b: Given I am on the landing page,
     *     when I view the footer,
     *     then a Privacy Policy link is visible.
     */
    @Test
    void testPrivacyPolicyVisible() {
        assertTrue(lookup("#privacyLink").tryQuery().isPresent(),
                "Privacy Policy link should be visible in footer");
    }

    /**
     * 6a: Given I am on the landing page,
     *     when I view the header,
     *     then the Help button is visible.
     */
    @Test
    void testHelpButtonVisible() {
        assertTrue(lookup("#helpBtn").tryQuery().isPresent(),
                "Help button should be visible in header");
    }

    /**
     * 6b: Given I am on the landing page,
     *     when I click the Help button,
     *     then the Help dialog opens.
     */
    @Test
    void testHelpDialogOpens() {
        clickOn("#helpBtn");
        assertTrue(lookup("#helpDialog").tryQuery().isPresent(),
                "Clicking Help should open a help dialog");
    }

    /**
     * 7a: Given I am on the login page,
     *     when I view the page,
     *     then the Login button is not visible anymore.
     */
    @Test
    void testLoginButtonNotVisibleOnLoginPage() {
        clickOn("#loginBtn"); // go to login page
        assertTrue(lookup("#loginBtn").tryQuery().isEmpty(),
                "Login button should not be visible on login page");
    }

    /**
     * 7b: Given I am on the landing page,
     *     when I view the hero section,
     *     then the hero text is visible.
     */
    @Test
    void testHeroTextVisible() {
        assertTrue(lookup("#heroText").tryQuery().isPresent(),
                "Hero text should be visible in hero section");
    }

    /**
     * 8a: Given I am on the landing page,
     *     when I view the navigation menu,
     *     then a Home link is present.
     */
    @Test
    void testHomeLinkInNavigationMenu() {
        assertTrue(lookup("#homeLink").tryQuery().isPresent(),
                "Home link should be present in navigation menu");
    }

    /**
     * 8b: Given I am on the landing page,
     *     when I click the Home link,
     *     then I remain on the landing page.
     */
    @Test
    void testClickingHomeLinkStaysOnLandingPage() {
        clickOn("#homeLink");
        assertTrue(lookup("#loginBtn").tryQuery().isPresent(),
                "Clicking Home should keep user on landing page");
    }

    /**
     * 9a: Given I am on the landing page,
     *     when I view the footer,
     *     then a Contact Us link is visible.
     */
    @Test
    void testContactUsLinkVisible() {
        assertTrue(lookup("#contactLink").tryQuery().isPresent(),
                "Contact Us link should be visible in footer");
    }

    /**
     * 9b: Given I am on the landing page,
     *     when I click the Contact Us link,
     *     then a Contact page or form opens.
     */
    @Test
    void testContactUsRedirectsToContactPage() {
        clickOn("#contactLink");
        assertTrue(lookup("#contactForm").tryQuery().isPresent(),
                "Clicking Contact Us should open contact form or page");
    }

    /**
     * 10a: Given I am on the landing page,
     *      when I view the page,
     *      then a language selector is visible.
     */
    @Test
    void testLanguageSelectorVisible() {
        assertTrue(lookup("#languageSelector").tryQuery().isPresent(),
                "Language selector should be visible on landing page");
    }

    /**
     * 10b: Given I am on the landing page,
     *      when I change the language selector,
     *      then the page updates to the selected language.
     */
    @Test
    void testLanguageChangeUpdatesPage() {
        clickOn("#languageSelector");
        clickOn("Español"); // assume Spanish is in dropdown
        assertTrue(lookup("#heroTextEs").tryQuery().isPresent(),
                "Selecting Español should update page text");
    }

    /**
     * 11a: Given I am on the landing page,
     *      when I view the header,
     *      then a search field is visible.
     */
    @Test
    void testSearchFieldVisible() {
        assertTrue(lookup("#searchField").tryQuery().isPresent(),
                "Search field should be visible in header");
    }

    /**
     * 11b: Given I am on the landing page,
     *      when I type into the search field,
     *      then the search suggestions appear.
     */
    @Test
    void testSearchSuggestionsAppear() {
        clickOn("#searchField").write("sign");
        assertTrue(lookup("#searchSuggestions").tryQuery().isPresent(),
                "Typing in search should show suggestions");
    }

    /**
     * 12: Given I am on the landing page,
     *     when I resize the window,
     *     then the layout adapts (responsive design).
     */
    @Test
    void testResponsiveLayoutOnResize() {
        javafx.stage.Window window = lookup(".stage").query();
        window.setWidth(300);
        window.setHeight(500);
        assertTrue(lookup("#mobileMenu").tryQuery().isPresent(),
                "Mobile menu should appear when window is resized");
    }
}