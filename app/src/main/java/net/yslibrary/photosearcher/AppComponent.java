package net.yslibrary.photosearcher;

import com.squareup.otto.Bus;

import net.yslibrary.photosearcher.graph.module.AppModule;
import net.yslibrary.photosearcher.graph.module.DataModule;
import net.yslibrary.photosearcher.graph.module.WebModule;
import net.yslibrary.photosearcher.model.TimelineManager;
import net.yslibrary.photosearcher.model.api.RxFavoriteService;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yshrsmz on 15/08/24.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                WebModule.class,
                DataModule.class
        }
)
public interface AppComponent {

    void inject(App app);

    Application application();

    RxFavoriteService rxFavoriteService();

    TimelineManager timelineManager();

    Bus bus();
}
