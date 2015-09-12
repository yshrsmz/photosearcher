package net.yslibrary.photosearcher.graph.module;

import net.yslibrary.photosearcher.graph.qualifier.PerActivity;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yshrsmz on 15/08/29.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity activity() {
        return activity;
    }
}
