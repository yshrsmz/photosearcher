package net.yslibrary.photosearcher.ui.view;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by yshrsmz on 15/08/26.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int DEFAULT_LOADING_TRIGGER_OFFSET_SIZE = 5;

    private static final long DELAY_MILLS_TO_CHECK_NEXT_LOAD = 300;

    StaggeredGridLayoutManager mManager;

    private Handler mHandler = new Handler();

    public EndlessScrollListener(StaggeredGridLayoutManager manager) {
        mManager = manager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            checkNextLoad(mManager);
        }
    }

    public void checkNextLoad(@NonNull StaggeredGridLayoutManager manager) {
        if (shouldLoadNext(manager)) {
            onLoadAdditionalData();
        }
    }

    public void checkNextLoadWithDelay(@NonNull StaggeredGridLayoutManager manager) {
        // check if result fill all the page, otherwise load additional data
        // must delay execution to wait unsubscribe subscription
        mHandler.postDelayed(() -> checkNextLoad(manager), DELAY_MILLS_TO_CHECK_NEXT_LOAD);
    }

    public boolean shouldLoadNext(@NonNull StaggeredGridLayoutManager manager) {
        int lastItemPosition = manager.getItemCount() - 1;
        int[] lastVisibleItemPositions = manager.findLastVisibleItemPositions(null);

        if (lastVisibleItemPositions.length == 0) {
            return true;
        }

        int lastVisibleItemPosition = lastVisibleItemPositions[lastVisibleItemPositions.length - 1];

        return (lastItemPosition < lastVisibleItemPosition + getLoadingTriggerOffsetSize());
    }

    public int getLoadingTriggerOffsetSize() {
        return DEFAULT_LOADING_TRIGGER_OFFSET_SIZE;
    }

    protected abstract void onLoadAdditionalData();
}
