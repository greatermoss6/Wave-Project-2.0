package com.signlearn.presentation.controller;

import com.signlearn.app.router.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutController extends BaseController {

    @FXML private Button homeBtn, contactBtn, loginBtn, signupBtn, aboutSignupBtn, aboutContactBtn;

    @Override
    public void postInit() {
        homeBtn.setOnAction(e -> router.goTo(View.LANDING));
        contactBtn.setOnAction(e -> router.goTo(View.CONTACT));
        loginBtn.setOnAction(e -> router.goTo(View.LOGIN));
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        aboutSignupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        aboutContactBtn.setOnAction(e -> router.goTo(View.CONTACT));
    }
}

