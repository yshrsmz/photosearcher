package net.yslibrary.instasearcher.module;

import com.squareup.okhttp.OkHttpClient;

import android.app.Application;

import java.net.CookieHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shimizu_yasuhiro on 15/08/24.
 */
@Module
public class WebModule {

    @Singleton
    @Provides
    public OkHttpClient okHttpClient(Application app) {
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.setCookieHandler(CookieHandler.getDefault());

        return okHttpClient;
    }
}
