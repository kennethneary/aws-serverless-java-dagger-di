package com.serverless.config.dagger;

public class Injector {
    private static AppComponent appComponent;

    public static AppComponent getInjector() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.create();
        }
        return appComponent;
    }
}
