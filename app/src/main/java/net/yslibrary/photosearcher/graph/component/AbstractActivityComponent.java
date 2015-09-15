package net.yslibrary.photosearcher.graph.component;

import net.yslibrary.photosearcher.AppComponent;
import net.yslibrary.photosearcher.graph.module.ActivityModule;
import net.yslibrary.photosearcher.graph.qualifier.PerActivity;

import android.app.Activity;

import dagger.Component;

/**
 * Created by yshrsmz on 15/08/29.
 */
@PerActivity // Subtypes of AbstractActivityComponent should be decorted with @PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface AbstractActivityComponent {
    Activity activity();
}
