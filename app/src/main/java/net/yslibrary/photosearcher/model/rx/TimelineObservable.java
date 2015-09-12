package net.yslibrary.photosearcher.model.rx;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.internal.TimelineStateHolder;

import net.yslibrary.photosearcher.model.IDataSetManager;
import net.yslibrary.photosearcher.model.dto.DataSetInsertResult;
import net.yslibrary.photosearcher.model.entity.FavoriteTweet;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.subjects.PublishSubject;


/**
 * Created by yshrsmz on 15/08/26.
 */
public class TimelineObservable implements IDataSetManager<Tweet> {

    private PublishSubject<DataSetInsertResult> mNextSubject = PublishSubject.create();

    private PublishSubject<DataSetInsertResult> mPreviousSubject = PublishSubject.create();

    private Timeline<Tweet> mTimeline;

    private TimelineStateHolder mTimelineStateHolder;

    private List<Tweet> mTweets = new ArrayList<>();

    private boolean mRefreshing = false;

    private TimelineObservable(Timeline<Tweet> timeline) {
        mTimeline = timeline;
        mTimelineStateHolder = new TimelineStateHolder();
        refresh();
    }

    public static TimelineObservable create(Timeline<Tweet> timeline) {
        return new TimelineObservable(timeline);
    }

    /**
     * Get Observable for Next Timeline Request
     */
    public Observable<DataSetInsertResult> getNextObservable() {
        return mNextSubject.asObservable();
    }

    /**
     * Get Observable for Previous Timeline Request
     */
    public Observable<DataSetInsertResult> getPreviousObservable() {
        return mPreviousSubject.asObservable();
    }

    public void next(Long maxPosition) {
        if (!mTimelineStateHolder.startTimelineRequest()) {
            return;
        }
        mTimeline.next(maxPosition, new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                mTimelineStateHolder.setNextCursor(result.data.timelineCursor);
                mTweets.addAll(0, result.data.items);

                mNextSubject.onNext(new DataSetInsertResult(0, result.data.items.size()));

                mTimelineStateHolder.finishTimelineRequest();
            }

            @Override
            public void failure(TwitterException e) {
                mNextSubject.onError(e);

                mTimelineStateHolder.finishTimelineRequest();
            }
        });
    }

    public void next() {
        next(getPositionForNext());
    }

    public void previous(Long minPosition) {
        if (!mTimelineStateHolder.startTimelineRequest()) {
            return;
        }
        fetchPrevious(minPosition);
    }

    public boolean refresh() {
        if (!mTimelineStateHolder.startTimelineRequest()) {
            return false;
        }

        mTimelineStateHolder.resetCursors();
        fetchPrevious(null);

        return true;
    }

    private void fetchPrevious(Long minPosition) {
        mTimeline.previous(minPosition, new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                mTimelineStateHolder.setPreviousCursor(result.data.timelineCursor);
                if (mRefreshing) {
                    mTweets.clear();
                }
                int position = mTweets.size();
                mTweets.addAll(result.data.items);

                mPreviousSubject.onNext(new DataSetInsertResult(position, result.data.items.size()));

                mTimelineStateHolder.finishTimelineRequest();
                mRefreshing = false;
            }

            @Override
            public void failure(TwitterException e) {
                mPreviousSubject.onError(e);

                mTimelineStateHolder.finishTimelineRequest();
                mRefreshing = false;
            }
        });
    }

    public void previous() {
        previous(getPositionForPrevious());
    }

    public Long getPositionForNext() {
        return mTimelineStateHolder.positionForNext();
    }

    public Long getPositionForPrevious() {
        return mTimelineStateHolder.positionForPrevious();
    }

    @Override
    public Tweet getItem(int position) {
        Tweet plain = mTweets.get(position);

        // check if favorited
        Realm realm = Realm.getDefaultInstance();
        boolean favorited = realm.where(FavoriteTweet.class).equalTo("id", plain.id).count() > 0;
        realm.close();

        Tweet result = new Tweet(
                plain.coordinates,
                plain.createdAt,
                plain.currentUserRetweet,
                plain.entities,
                plain.favoriteCount,
                favorited && plain.favoriteCount > 0,
                plain.filterLevel,
                plain.id,
                plain.idStr,
                plain.inReplyToScreenName,
                plain.inReplyToStatusId,
                plain.inReplyToStatusIdStr,
                plain.inReplyToUserId,
                plain.inReplyToUserIdStr,
                plain.lang,
                plain.place,
                plain.possiblySensitive,
                plain.scopes,
                plain.retweetCount,
                plain.retweeted,
                plain.retweetedStatus,
                plain.source,
                plain.text,
                plain.truncated,
                plain.user,
                plain.withheldCopyright,
                plain.withheldInCountries,
                plain.withheldScope);

        return result;
    }

    @Override
    public List<Tweet> getItems() {
        return mTweets;
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    @Override
    public int setItem(Tweet tweet) {
        int index = mTweets.indexOf(tweet);

        if (index > -1) {
            mTweets.set(index, tweet);
            return index;
        }

        return -1;
    }
}
