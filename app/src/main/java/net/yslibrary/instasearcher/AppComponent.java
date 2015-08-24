package net.yslibrary.instasearcher;

import net.yslibrary.instasearcher.module.AppModule;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by shimizu_yasuhiro on 15/08/24.
 */
@Singleton
@Component(modules = {
        AppModule.class
})
public interface AppComponent {

    void inject(App app);

    Application application();
}
