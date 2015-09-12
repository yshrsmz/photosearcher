package net.yslibrary.photosearcher;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.okhttp.OkHttpClient;

import net.yslibrary.photosearcher.graph.module.AppModule;

/**
 * Created by shimizu_yasuhiro on 15/08/24.
 */
public class DebugAppModule extends AppModule {

    public DebugAppModule(App app) {
        super(app);
    }

    @Override
    protected App.LifecycleCallbacks lifecycleCallbacks(OkHttpClient okHttpClient) {
        return new DebugAppLifecycleCallbacks(mApp, okHttpClient);
    }

    @Override
    protected RefWatcher refWatcher() {
        return LeakCanary.install(mApp);
    }
}
