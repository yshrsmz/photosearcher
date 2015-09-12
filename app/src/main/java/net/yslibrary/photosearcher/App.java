package net.yslibrary.photosearcher;

import net.yslibrary.photosearcher.util.RealmUtil;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

/**
 * Created by yshrsmz on 15/08/24.
 */
public class App extends Application {

    @Inject
    LifecycleCallbacks mLifecycleCallbacks;

    private AppComponent mComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RealmUtil.setupDatabase(this);

        setupComponent();
        mLifecycleCallbacks.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mLifecycleCallbacks.onTerminate();
    }

    private void setupComponent() {
        mComponent = DaggerAppComponent.builder()
                .appModule(Modules.appModule(this))
                .build();

        mComponent.inject(this);
    }

    public AppComponent getComponent() {
        return mComponent;
    }

    public interface LifecycleCallbacks {

        void onCreate();

        void onTerminate();
    }
}
