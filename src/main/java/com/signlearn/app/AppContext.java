package com.signlearn.app;

import com.signlearn.app.router.SceneRouter;

public class AppContext {
    private static SceneRouter router;

    public static void setRouter(SceneRouter r) {
        router = r;
    }

    public static SceneRouter getRouter() {
        return router;
    }
}