package net.yslibrary.instasearcher;

import net.yslibrary.instasearcher.module.AppModule;

public class Modules {

    private Modules() {
        // empty
    }

    public static AppModule appModule(App app) {
        return new AppModule(app);
    }
}