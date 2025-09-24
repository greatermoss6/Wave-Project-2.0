package com.signlearn.presentation.ui;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class WindowManager {
    private final Stage stage;
    private final AtomicBoolean isResizing = new AtomicBoolean(false);

    public WindowManager(Stage stage) {
        this.stage = stage;
        setupListeners();
    }

    private void setupListeners() {
        // Handle restore down when leaving maximized state
        stage.maximizedProperty().addListener((obs, wasMaximized, isMaximized) -> {
            if (wasMaximized && !isMaximized && !isResizing.get()) {
                isResizing.set(true);
                Platform.runLater(() -> restoreDown());
            }
        });
    }

    private Rectangle2D getCurrentScreenBounds() {
        return Screen.getScreensForRectangle(
                        stage.getX(),
                        stage.getY(),
                        stage.getWidth(),
                        stage.getHeight()
                ).stream()
                .findFirst()
                .map(Screen::getVisualBounds)
                .orElse(Screen.getPrimary().getVisualBounds());
    }

    private void restoreDown() {
        Rectangle2D screenBounds = getCurrentScreenBounds();

        double targetWidth = screenBounds.getWidth() * 0.8;
        double targetHeight = screenBounds.getHeight() * 0.8;

        // Clamp size so it doesn’t get extreme
        targetWidth = Math.max(1024, Math.min(1920, targetWidth));
        targetHeight = Math.max(600, Math.min(1080, targetHeight));

        // Apply new size & recenter
        stage.setWidth(targetWidth);
        stage.setHeight(targetHeight);
        stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - targetWidth) / 2);
        stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - targetHeight) / 2);

        // Reset debounce flag after short delay
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> isResizing.set(false));
        }).start();
    }

    public void maximize() {
        Platform.runLater(() -> stage.setMaximized(true));
    }

    public void restoreToDefault() {
        restoreDown();
    }
}