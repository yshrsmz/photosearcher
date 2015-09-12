package net.yslibrary.photosearcher.graph.module;

import com.squareup.leakcanary.RefWatcher;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import net.yslibrary.photosearcher.App;
import net.yslibrary.photosearcher.AppLifecycleCallbacks;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yshrsmz on 15/08/24.
 */
@Module
public class AppModule {

    protected final App mApp;

    public AppModule(App application) {
        mApp = application;
    }

    @Provides
    @Singleton
    protected Application application() {
        return mApp;
    }

    @Provides
    @Singleton
    protected App.LifecycleCallbacks lifecycleCallbacks(OkHttpClient okHttpClient) {
        return new AppLifecycleCallbacks(mApp, okHttpClient);
    }

    @Provides
    @Singleton
    protected RefWatcher refWatcher() {
        return RefWatcher.DISABLED;
    }

    @Provides
    @Singleton
    protected Bus bus() {
        return new Bus();
    }
}
