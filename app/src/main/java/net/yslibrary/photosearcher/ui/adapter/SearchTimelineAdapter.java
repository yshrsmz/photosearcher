package net.yslibrary.photosearcher.ui.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;

import net.yslibrary.photosearcher.R;
import net.yslibrary.photosearcher.model.IDataSetManager;
import net.yslibrary.photosearcher.model.api.Urls;
import net.yslibrary.photosearcher.model.enums.MediaSize;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.functions.Action1;
import rx.functions.Action2;
import timber.log.Timber;

/**
 * Created by yshrsmz on 15/08/26.
 */
public class SearchTimelineAdapter extends RecyclerView.Adapter<SearchTimelineAdapter.ViewHolder> {

    private final int mColumnCount;

    private final int WIDTH_INDEX = 0;

    private final int HEIGHT_INDEX = 1;

    private IDataSetManager<Tweet> mDataSetManager;
    private Action1<Long> mOnFavoriteClick;
    private Action2<String, Long> mOnClick;

    public SearchTimelineAdapter(int columnCount, IDataSetManager<Tweet> dataSetManager) {
        mColumnCount = columnCount;
        mDataSetManager = dataSetManager;
    }

    public SearchTimelineAdapter setFavoriteListener(Action1<Long> onFavoriteClick) {
        mOnFavoriteClick = onFavoriteClick;

        return this;
    }

    public SearchTimelineAdapter setOnClickListener(Action2<String, Long> onClick) {
        mOnClick = onClick;

        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int width = viewGroup.getWidth() / mColumnCount;
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tweet, viewGroup, false);
        return new ViewHolder(view, width);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Tweet tweet = mDataSetManager.getItem(i);
        View view = viewHolder.itemView;
        int[] viewMetrics = new int[2];
        int mediaCount = 1;
        String url;
        if (tweet.entities.media == null) {
            if (tweet.entities.urls.size() > 0) {
                Uri uri = Uri.parse(tweet.entities.urls.get(0).expandedUrl);
                if (uri.getHost().equals(Urls.TWITPIC_HOST)) {
                    Timber.d(tweet.entities.urls.get(0).expandedUrl);
                    String id = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
                    url = String.format(Urls.TWITPIC_THUMB_URL, id);
                } else {
                    url = null;
                }
            } else {
                url = null;
            }
            viewMetrics[HEIGHT_INDEX] = viewHolder.mWidth;
            viewMetrics[WIDTH_INDEX] = viewHolder.mWidth;
        } else {
            MediaEntity media = tweet.entities.media.get(0);
            MediaSize imageSize = getAppropriateSize(viewHolder.mWidth, media);
            viewMetrics = getViewMetrics(viewHolder.mWidth, imageSize.getSize(media));
            url = media.mediaUrl + ":" + imageSize.toString();
            mediaCount = tweet.entities.media.size();
        }


        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = viewMetrics[HEIGHT_INDEX] > viewHolder.mWidth ? viewMetrics[HEIGHT_INDEX] : viewHolder.mWidth;
        view.setLayoutParams(lp);

        ViewGroup.LayoutParams thumbLp = viewHolder.mThumb.getLayoutParams();
        thumbLp.height = viewMetrics[HEIGHT_INDEX];
        thumbLp.width = viewMetrics[WIDTH_INDEX];
        viewHolder.mThumb.setLayoutParams(thumbLp);

        viewHolder.mName.setText(tweet.user.name);

        if (mediaCount > 1) {
            viewHolder.mMediaCount.setText(String.format("(%d)", mediaCount));
            viewHolder.mMediaCount.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mMediaCount.setVisibility(View.GONE);
        }


        Glide.with(viewHolder.itemView.getContext())
                .load(url)
                .error(R.drawable.ic_close_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.mThumb);

        viewHolder.mFavoriteButton.setSelected(tweet.favorited);
        viewHolder.mFavoriteButton.setEnabled(true);

        viewHolder.mFavorited = tweet.favorited;
        viewHolder.mTweetId = tweet.id;
        viewHolder.mUserScreenName = tweet.user.screenName;
    }

    @Override
    public int getItemCount() {
        return mDataSetManager.getItemCount();
    }

    /**
     * swap tweet by new tweet
     *
     * @param tweet new tweet
     */
    public void replaceTweet(Tweet tweet) {
        int index = mDataSetManager.setItem(tweet);
        if (index > -1) {
            notifyItemChanged(index);
        }
    }

    /**
     * get Appropriate MediaSize for the view
     *
     * @param viewWidth target view width
     * @param media     media to show
     * @return MediaSize
     */
    private MediaSize getAppropriateSize(int viewWidth, MediaEntity media) {
        List<MediaEntity.Size> sizes = new ArrayList<>(
                Arrays.asList(media.sizes.thumb,
                        media.sizes.small,
                        media.sizes.medium,
                        media.sizes.large));
        int distance = Math.abs(media.sizes.thumb.w - viewWidth);
        int resultIndex = 0;

        for (int i = 1; i < sizes.size(); i++) {
            int iDistance = Math.abs(sizes.get(i).w - viewWidth);
            if (iDistance < distance) {
                resultIndex = i;
                distance = iDistance;
            }
        }

        return MediaSize.values()[resultIndex];
    }

    /**
     * get appropriate width/height for view
     *
     * @param viewWidth target view width
     * @param size      target media's Size entity
     * @return int[]{0: width, 1: height}
     */
    private int[] getViewMetrics(int viewWidth, MediaEntity.Size size) {
        int[] result = new int[2];
        if (size.w < viewWidth) {
            result[WIDTH_INDEX] = size.w;
            result[HEIGHT_INDEX] = size.h;
        } else {
            result[WIDTH_INDEX] = viewWidth;
            result[HEIGHT_INDEX] = (int) (size.h * ((float) viewWidth / (float) size.w));
        }

        return result;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.itemName)
        TextView mName;

        @Bind(R.id.itemMediaCount)
        TextView mMediaCount;

        @Bind(R.id.itemThumb)
        ImageView mThumb;

        @Bind(R.id.itemFavoriteBtn)
        ImageButton mFavoriteButton;

        int mWidth;
        Long mTweetId;
        String mUserScreenName;
        boolean mFavorited;

        public ViewHolder(View itemView, int width) {
            super(itemView);

            mWidth = width;
            ButterKnife.bind(this, itemView);
        }

        @OnLongClick(R.id.itemRoot)
        public boolean onLongClick() {
            Timber.d("long click");

            mFavoriteButton.setSelected(true);
            mFavoriteButton.setEnabled(false);
            if (mOnFavoriteClick != null) {
                mOnFavoriteClick.call(mTweetId);
            }

            return true;
        }

        @OnClick(R.id.itemFavoriteBtn)
        public void onFavoriteClick() {
            Timber.d("favorite click");

            mFavoriteButton.setSelected(true);
            mFavoriteButton.setEnabled(false);

            if (mOnFavoriteClick != null) {
                mOnFavoriteClick.call(mTweetId);
            }
        }

        @OnClick(R.id.itemRoot)
        public void onClick() {
            Timber.d("click");

            if (mOnClick != null) {
                mOnClick.call(mUserScreenName, mTweetId);
            }
        }
    }
}
