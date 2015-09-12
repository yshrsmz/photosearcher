package net.yslibrary.photosearcher.model;

import com.twitter.sdk.android.tweetui.SearchTimeline;

import net.yslibrary.photosearcher.model.entity.SearchQuery;
import net.yslibrary.photosearcher.model.entity.SearchQueryList;
import net.yslibrary.photosearcher.model.rx.TimelineObservable;
import net.yslibrary.photosearcher.util.RealmUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Core class to manage multiple timeline
 * Created by yshrsmz on 15/08/27.
 */
@Singleton
public class TimelineManager implements ITabDataSetManager<String> {

    Map<String, TimelineObservable> mTimelineMap = new LinkedHashMap<>();

    public TimelineManager() {
        restoreTimelines();
    }

    private void restoreTimelines() {
//        insertTestData();
        // restore saved timelines from realm

        RealmUtil.executeTransaction(realm -> {
            SearchQueryList queries = realm.where(SearchQueryList.class).findFirst();
            if (queries == null) {
                queries = realm.createObject(SearchQueryList.class);
            }
            for (SearchQuery query : queries.getSearchQueries()) {
                String queryStr = query.getQuery();
                mTimelineMap.put(
                        queryStr,
                        TimelineObservable.create(createSearchTimeline(queryStr)));
            }
        }, throwable -> Timber.e(throwable, throwable.getMessage()));
    }

    private SearchTimeline createSearchTimeline(String query) {
        return new SearchTimeline.Builder()
                .query(query + "+filter:images")
                .build();
    }

    public TimelineObservable addTimeline(String query) {
        final boolean[] exists = {false};

        RealmUtil.executeTransaction(realm -> {
            exists[0] = realm.where(SearchQuery.class).equalTo("query", query).count() > 0;
            SearchQueryList list = realm.where(SearchQueryList.class).findFirst();
            if (list == null) {
                list = realm.createObject(SearchQueryList.class);
            }
            if (!exists[0]) {
                SearchQuery q = new SearchQuery();
                q.setQuery(query);

                realm.copyToRealm(q);
                list.getSearchQueries().add(q);
            }
        }, throwable -> {
            Timber.e(throwable, throwable.getMessage());
        });

        if (!exists[0]) {
            TimelineObservable observable = TimelineObservable.create(createSearchTimeline(query));
            mTimelineMap.put(query, observable);
            return observable;
        } else {
            return null;
        }
    }

    public int deleteTimeline(String query) {
        int position = getQueries().indexOf(query);

        RealmUtil.executeTransaction(realm -> {
            realm.where(SearchQuery.class).equalTo("query", query).findAll().clear();
        }, throwable -> {
            Timber.e(throwable, throwable.getMessage());
        });
        mTimelineMap.remove(query);

        return position;
    }

    public List<String> getQueries() {
        return new ArrayList<>(mTimelineMap.keySet());
    }

    public TimelineObservable getTimelineObservable(String key) {
        return mTimelineMap.get(key);
    }

    public TimelineObservable getTimelineObservable(int index) {
        if (index >= mTimelineMap.size()) {
            return null;
        }
        return (TimelineObservable) mTimelineMap.values().toArray()[index];
    }

    @Override
    public String getItem(int position) {
        return getQueries().get(position);
    }

    @Override
    public int getCount() {
        return mTimelineMap.size();
    }

    public void moveItem(int from, int to) {
        Map<String, TimelineObservable> oldMap = mTimelineMap;
        Map<String, TimelineObservable> newMap = new LinkedHashMap<>();
        List<String> keys = getQueries();
        String targetItem = keys.get(from);
        keys.remove(from);
        keys.add(to, targetItem);

        RealmUtil.executeTransaction(realm -> {
            SearchQueryList list = realm.where(SearchQueryList.class).findFirst();
            list.getSearchQueries().clear();
            for (String key : keys) {
                SearchQuery query = realm.where(SearchQuery.class).equalTo("query", key)
                        .findFirst();
                list.getSearchQueries().add(query);
                newMap.put(key, oldMap.get(key));
            }

            mTimelineMap = newMap;
        }, throwable -> {
            Timber.e(throwable, throwable.getMessage());
        });
    }

    public void insertTestData() {
        List<String> data = new ArrayList<String>(Arrays.asList("#ilovecat", "#cat", "#ferret"));
        RealmUtil.executeTransaction(realm -> {
            SearchQueryList list = realm.where(SearchQueryList.class).findFirst();
            if (list == null) {
                list = realm.createObject(SearchQueryList.class);
            }
            for (String query : data) {
                SearchQuery q = new SearchQuery();
                q.setQuery(query);
                realm.copyToRealmOrUpdate(q);
                list.getSearchQueries().add(q);
            }
        }, throwable -> Timber.d(throwable, throwable.getMessage()));
    }
}
