package com.signlearn.presentation.controller;

import com.signlearn.app.router.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ContactController extends BaseController {

    @FXML private Button landingBtn;
    @FXML private Button aboutBtn;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;

    @Override
    public void postInit() {
        landingBtn.setOnAction(e -> router.goTo(View.LANDING));
        aboutBtn.setOnAction(e -> router.goTo(View.ABOUT));
        loginBtn.setOnAction(e -> router.goTo(View.LOGIN));
        signupBtn.setOnAction(e -> router.goTo(View.SIGNUP_SHALLOW));
    }
}