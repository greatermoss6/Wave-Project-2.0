package com.signlearn.presentation.controller;

import com.signlearn.app.router.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutController extends BaseController {

    @FXML private Button landingBtn;
    @FXML private Button contactBtn;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;

    @Override
    public void postInit() {
        landingBtn.setOnAction(e -> router.goTo(View.LANDING));
        contactBtn.setOnAction(e -> router.goTo(View.CONTACT));
        loginBtn.setOnAction(e -> router.goTo(View.LOGIN));
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
    }
}

