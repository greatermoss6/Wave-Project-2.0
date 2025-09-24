package com.signlearn.presentation.controller;

import com.signlearn.app.router.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ContactController extends BaseController {

    @FXML private Button homeBtn, aboutBtn, loginBtn, signupBtn, contactSignupBtn, contactAboutBtn;

    @Override
    public void postInit() {
        homeBtn.setOnAction(e -> router.goTo(View.LANDING));
        aboutBtn.setOnAction(e -> router.goTo(View.ABOUT));
        loginBtn.setOnAction(e -> router.goTo(View.LOGIN));
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        contactSignupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
        contactAboutBtn.setOnAction(e -> router.goTo(View.ABOUT));
    }
}