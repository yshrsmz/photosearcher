package net.yslibrary.photosearcher.graph.qualifier;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by yshrsmz on 15/08/29.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
