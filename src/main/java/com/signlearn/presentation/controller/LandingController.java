package com.signlearn.presentation.controller;

import com.signlearn.app.router.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LandingController extends BaseController {

    @FXML private Button aboutBtn;
    @FXML private Button contactBtn;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;

    @Override
    public void postInit() {
        aboutBtn.setOnAction(e -> router.goTo(View.ABOUT));
        contactBtn.setOnAction(e -> router.goTo(View.CONTACT));
        loginBtn.setOnAction(e -> router.goTo(View.LOGIN));
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
    }
}
