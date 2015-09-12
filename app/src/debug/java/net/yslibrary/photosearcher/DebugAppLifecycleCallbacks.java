package net.yslibrary.photosearcher;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.OkHttpClient;

import timber.log.Timber;

/**
 * Created by shimizu_yasuhiro on 15/08/24.
 */
public class DebugAppLifecycleCallbacks extends AppLifecycleCallbacks {


    public DebugAppLifecycleCallbacks(App app, OkHttpClient okHttpClient) {
        super(app, okHttpClient);
    }

    @Override
    public void onCreate() {
        setupStetho();
        super.onCreate();
        setupLeakCanary();
    }

    @Override
    protected void setupLogger() {
        Timber.plant(new Timber.DebugTree());
    }

    protected void setupLeakCanary() {
        LeakCanary.install(mApp);
    }

    protected void setupStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(mApp)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(mApp))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(mApp))
                        .build());
    }
}
