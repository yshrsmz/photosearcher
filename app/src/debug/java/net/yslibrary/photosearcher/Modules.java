package net.yslibrary.photosearcher;

import net.yslibrary.photosearcher.graph.module.AppModule;

public class Modules {

    private Modules() {
        // empty
    }

    public static AppModule appModule(App app) {
        return new DebugAppModule(app);
    }
}
