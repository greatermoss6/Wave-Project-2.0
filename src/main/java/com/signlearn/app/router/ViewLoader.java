package com.signlearn.app.router;

import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.presentation.controller.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * ViewLoader constructs controllers via a ControllerFactory so we can
 * inject Router + Services *before* the controller's @FXML initialize() runs.
 * After FXML is loaded, it also triggers postInit() for controllers
 * that extend BaseController.
 */
public final class ViewLoader {

    private final ServiceRegistry registry;

    public ViewLoader(ServiceRegistry registry) {
        this.registry = registry;
    }

    /**
     * Loads the view and returns both the root node and the controller.
     * Dependencies are injected during controller construction, so
     * by the time initialize() runs, they're available.
     */
    public LoadedView load(View view, SceneRouter router) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));

            // Ensure controller is created by us so we can inject early
            loader.setControllerFactory(type -> {
                try {
                    Constructor<?> ctor = type.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    Object controller = ctor.newInstance();

                    if (controller instanceof RouterAware ra) {
                        ra.setRouter(router); // inject router first
                    }
                    if (controller instanceof DependencyAware da) {
                        da.injectDependencies(registry); // then services
                    }
                    return controller;
                } catch (Exception e) {
                    throw new RuntimeException("Failed to construct controller: " + type.getName(), e);
                }
            });

            Parent root = loader.load();
            Object controller = loader.getController();

            // 🔥 Call lifecycle hook AFTER injections + FXML wiring
            if (controller instanceof BaseController bc) {
                bc.postInit();
            }

            return new LoadedView(root, controller);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + view.getFxmlPath(), e);
        }
    }

    /** Small carrier for root + controller */
    public static final class LoadedView {
        public final Parent root;
        public final Object controller;

        public LoadedView(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        }
    }
}