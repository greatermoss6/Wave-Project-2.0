package com.signlearn.presentation.controller;

import com.signlearn.app.router.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LandingController extends BaseController {

    @FXML private Button aboutBtn;
    @FXML private Button contactBtn;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;
    @FXML private Button landingHeroSignUpBtn, landingHeroAboutBtn, landingFooterLoginBtn;

    @Override
    public void postInit() {
        aboutBtn.setOnAction(e -> router.goTo(View.ABOUT));
        contactBtn.setOnAction(e -> router.goTo(View.CONTACT));
        loginBtn.setOnAction(e -> router.goTo(View.LOGIN));
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));

        if (landingHeroSignUpBtn != null)
        {
            landingHeroSignUpBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        }
        if (landingHeroAboutBtn != null)
        {
            landingHeroAboutBtn.setOnAction(actionEvent -> router.goTo(View.ABOUT));
        }
        if (landingFooterLoginBtn != null)
        {
            landingFooterLoginBtn.setOnAction(actionEvent -> router.goTo(View.LOGIN));
        }
    }
}
