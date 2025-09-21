package com.signlearn.app.router;

import com.signlearn.app.dependency_injection.ServiceRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * SceneRouter now ONLY switches scenes.
 * ViewLoader handles FXML loading and dependency injection.
 */
public class SceneRouter {
    private final Stage stage;
    private final Scene scene;
    private final ViewLoader viewLoader;

    public SceneRouter(Stage stage, ServiceRegistry serviceRegistry, View initialView) {
        this.stage = stage;
        this.viewLoader = new ViewLoader(serviceRegistry);

        ViewLoader.LoadedView loaded = viewLoader.load(initialView, this);
        this.scene = new Scene(loaded.root);
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void goTo(View view) {
        goTo(view, null);
    }

    public void goTo(View view, Consumer<Object> controllerInitializer) {
        ViewLoader.LoadedView loaded = viewLoader.load(view, this);

        if (controllerInitializer != null && loaded.controller != null) {
            controllerInitializer.accept(loaded.controller);
        }

        // Swap the root; keep the same Scene to preserve window state
        Scene current = stage.getScene();
        if (current == null) {
            stage.setScene(new Scene(loaded.root));
        } else {
            current.setRoot(loaded.root);
        }

        // TEST: super-visible breadcrumb for manual verification
        stage.setTitle("Wave • " + view.name());            // TEST: window title shows current view
        System.out.println("[NAV] -> " + view.name());      // TEST: console breadcrumb
    }
}
