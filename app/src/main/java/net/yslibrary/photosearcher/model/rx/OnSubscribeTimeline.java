package net.yslibrary.photosearcher.model.rx;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yshrsmz on 15/08/26.
 */
public class OnSubscribeTimeline<T extends Timeline<Tweet>> implements Observable.OnSubscribe<T> {
    @Override
    public void call(Subscriber<? super T> subscriber) {

    }
}
