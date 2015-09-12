package net.yslibrary.photosearcher.graph.module;

import net.yslibrary.photosearcher.model.TimelineManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yshrsmz on 15/08/29.
 */
@Module
public class DataModule {

    public DataModule() {
    }

    @Provides
    @Singleton
    TimelineManager timelineManager() {
        return new TimelineManager();
    }
}
