package com.signlearn.app.router;

public enum View {
    LANDING("/fxml/landing.fxml"),
    ABOUT("/fxml/about.fxml"),
    CONTACT("/fxml/contact.fxml"),
    SIGNUP_SHALLOW("/fxml/signup_shallow.fxml"),
    SIGNUP_DEEP("/fxml/signup_deep.fxml"),
    LOGIN("/fxml/login.fxml"),
    LEARNING("/fxml/learning.fxml"),
    VIDEO("/fxml/video_lesson.fxml"),
    QUESTION("/fxml/question.fxml");

    private final String fxmlPath;

    View(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}