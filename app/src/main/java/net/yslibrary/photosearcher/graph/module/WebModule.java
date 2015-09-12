package net.yslibrary.photosearcher.graph.module;

import com.squareup.okhttp.OkHttpClient;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;

import net.yslibrary.photosearcher.model.api.RxFavoriteService;
import net.yslibrary.photosearcher.model.api.RxTwitterApiClient;

import android.app.Application;

import java.net.CookieHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yshrsmz on 15/08/24.
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

    @Provides
    @Singleton
    public RxTwitterApiClient rxTwitterApiClient() {
        return new RxTwitterApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession());
    }

    @Provides
    @Singleton
    public RxFavoriteService rxFavoriteService(RxTwitterApiClient apiClient) {
        return apiClient.getRxFavoriteService();
    }

    @Provides
    @Singleton
    public TwitterApiClient twitterApiClient() {
        return TwitterCore.getInstance().getApiClient();
    }
}
