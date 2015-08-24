package net.yslibrary.instasearcher.module;

import com.squareup.leakcanary.RefWatcher;
import com.squareup.okhttp.OkHttpClient;

import net.yslibrary.instasearcher.App;
import net.yslibrary.instasearcher.AppLifecycleCallbacks;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shimizu_yasuhiro on 15/08/24.
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

    protected RefWatcher refWatcher() {
        return RefWatcher.DISABLED;
    }
}
