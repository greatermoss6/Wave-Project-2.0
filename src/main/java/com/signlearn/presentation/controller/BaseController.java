// src/main/java/com/signlearn/presentation/controller/BaseController.java
package com.signlearn.presentation.controller;

import com.signlearn.app.router.RouterAware;
import com.signlearn.app.router.SceneRouter;
import javafx.scene.control.Alert;

/**
 * Base class for all controllers.
 * Provides router injection, lifecycle hook, and common UI helpers.
 */
public abstract class BaseController implements RouterAware {
    protected SceneRouter router;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    /**
     * Lifecycle hook called by ViewLoader after all injections are complete.
     * Subclasses may override this instead of initialize() for logic
     * that depends on injected services.
     */
    public void postInit() {
        // no-op by default
    }

    /** Show an information dialog. */
    protected void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    /** Show an error dialog. */
    protected void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    /**
     * Back-compat convenience: delegate to showInfo so any existing
     * showAlert(...) calls keep working without edits elsewhere.
     */
    protected void showAlert(String title, String message) {
        showInfo(title, message);
    }
}