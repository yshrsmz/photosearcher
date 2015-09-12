package net.yslibrary.photosearcher.ui.fragment;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import net.yslibrary.photosearcher.R;
import net.yslibrary.photosearcher.event.SearchTimelineScrollEvent;
import net.yslibrary.photosearcher.model.FavoriteModel;
import net.yslibrary.photosearcher.model.TimelineManager;
import net.yslibrary.photosearcher.model.api.Urls;
import net.yslibrary.photosearcher.model.rx.TimelineObservable;
import net.yslibrary.photosearcher.ui.activity.MainActivity;
import net.yslibrary.photosearcher.ui.adapter.SearchTimelineAdapter;
import net.yslibrary.photosearcher.ui.adapter.decoration.GridLayoutSpacingDecoration;
import net.yslibrary.photosearcher.ui.view.EndlessScrollListener;
import net.yslibrary.photosearcher.ui.view.LoadingSpinner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


/**
 * Created by yshrsmz on 15/08/25.
 */
public class SearchTimelineFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final int COLUMN_COUNT = 2;

    @Bind(R.id.list)
    RecyclerView mList;

    @Bind(R.id.itemRoot)
    FrameLayout mRoot;

    @Bind(R.id.itemRefresh)
    SwipeRefreshLayout mRefreshLayout;

    @Inject
    FavoriteModel mFavoriteModel;

    @Inject
    Bus mBus;

    @Inject
    TimelineManager mTimelineManager;

    Subscription mPreviousSubscription = null;

    CompositeSubscription mSubscriptions = new CompositeSubscription();

    private LoadingSpinner mLoadingSpinner = new LoadingSpinner();

    private TimelineObservable mTimelineObservable;

    private StaggeredGridLayoutManager mLayoutManager;

    private SearchTimelineAdapter mAdapter;

    private String mQuery;

    public SearchTimelineFragment() {
        // empty
    }

    public static SearchTimelineFragment newInstance(String query) {
        SearchTimelineFragment fragment = new SearchTimelineFragment();

        Bundle args = new Bundle();
        args.putString(InitBundle.QUERY, query);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).getComponent().inject(this);

        if (mTimelineObservable == null) {
            mTimelineObservable = mTimelineManager.getTimelineObservable(mQuery);
            initList();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuery = getArguments().getString(InitBundle.QUERY);

        if (mTimelineObservable == null) {
//            SearchTimeline timeline = new SearchTimeline.Builder()
//                    .query(mQuery + "+filter:images")
//                    .build();
//            mTimelineObservable = TimelineObservable.create(timeline);
        }

        //TweetTimelineListAdapter
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_timeline, container, false);

        ButterKnife.bind(this, view);
        mRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initList() {
        mAdapter = new SearchTimelineAdapter(COLUMN_COUNT, mTimelineObservable)
                .setFavoriteListener(this::onFavorite)
                .setOnClickListener(this::onTweetClick);

        mLayoutManager = new StaggeredGridLayoutManager(COLUMN_COUNT,
                StaggeredGridLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(mLayoutManager);
        mList.addItemDecoration(new GridLayoutSpacingDecoration(
                getResources().getDimensionPixelSize(R.dimen.timeline_padding)));
        mList.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            protected void onLoadAdditionalData() {
                if (mTimelineObservable.getPositionForPrevious() != null) {
                    mTimelineObservable.previous();
                }
            }
        });
        mList.setAdapter(mAdapter);

        if (mAdapter.getItemCount() == 0) {
            mLoadingSpinner.show(this);
            mRefreshLayout.setEnabled(false);
        }
        mPreviousSubscription = mTimelineObservable.getPreviousObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSetInsertResult -> {
                    if (mRefreshLayout.isRefreshing()) {
                        mTimelineObservable.getItems().clear();
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                    mRefreshLayout.setEnabled(true);
                    mLoadingSpinner.dismiss();

                    if (dataSetInsertResult.getInsertedCount() > 0) {
                        mAdapter.notifyItemRangeInserted(
                                dataSetInsertResult.getInsertedPosition(),
                                dataSetInsertResult.getInsertedCount());
                    }
                }, throwable -> Timber.e(throwable, throwable.getMessage()));

        mSubscriptions.add(mPreviousSubscription);
    }

    public void onFavorite(Long tweetId) {
        Subscription subscription = mFavoriteModel.create(tweetId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tweet -> {
                            mAdapter.replaceTweet(tweet);
                            Snackbar.make(mRoot, R.string.favorite_complete_label,
                                    Snackbar.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Timber.e(throwable, throwable.getMessage());
                        });

        mSubscriptions.add(subscription);
    }

    public void onTweetClick(String userScreenName, Long tweetId) {
        // hopefully open twitter app
        Uri uri = Uri.parse(String.format(Urls.TWITTER_STATUS_URL, userScreenName, tweetId));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        mBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSubscriptions.unsubscribe();
    }

    @Override
    public void onRefresh() {
        if (!mTimelineObservable.refresh()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Subscribe
    public void onSearchTimelineScrollEvent(SearchTimelineScrollEvent event) {
        if (!mQuery.equals(event.query)) {
            return;
        }

        mList.smoothScrollToPosition(event.position);
    }

    interface InitBundle {

        String QUERY = "QUERY";
    }
}
