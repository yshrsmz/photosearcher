package net.yslibrary.photosearcher.graph.component;

import net.yslibrary.photosearcher.AppComponent;
import net.yslibrary.photosearcher.graph.module.ActivityModule;
import net.yslibrary.photosearcher.graph.qualifier.PerActivity;
import net.yslibrary.photosearcher.ui.activity.MainActivity;
import net.yslibrary.photosearcher.ui.fragment.SearchTimelineFragment;

import dagger.Component;

/**
 * Created by yshrsmz on 15/08/29.
 */
@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface MainComponent extends AbstractActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(SearchTimelineFragment fragment);
}
