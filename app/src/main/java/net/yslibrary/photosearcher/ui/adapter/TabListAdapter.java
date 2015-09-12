package net.yslibrary.photosearcher.ui.adapter;

import net.yslibrary.photosearcher.R;
import net.yslibrary.photosearcher.model.ITabDataSetManager;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by yshrsmz on 15/08/31.
 */
public class TabListAdapter extends RecyclerView.Adapter<TabListAdapter.ViewHolder>
        implements IItemTouchHelperAdapter {

    private ITabDataSetManager<String> mTabDataSetManager;
    private Action1<String> mOnDeleteQuery;
    private Action2<Integer, Integer> mOnMoveItem;
    private Action1<String> mOnItemClick;
    private OnStartViewHolderDragListener mDragStartListener;

    public TabListAdapter(ITabDataSetManager<String> tabDataSetManager,
                          OnStartViewHolderDragListener dragStartListener,
                          Action1<String> onDeleteQuery,
                          Action2<Integer, Integer> onMoveItem,
                          Action1<String> onItemClick) {
        mTabDataSetManager = tabDataSetManager;
        mDragStartListener = dragStartListener;
        mOnDeleteQuery = onDeleteQuery;
        mOnMoveItem = onMoveItem;
        mOnItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_search, parent, false);
        return new ViewHolder(view, mOnDeleteQuery);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String query = mTabDataSetManager.getItem(position);

        holder.mTitle.setText(query);
        holder.mPosition = position;
        holder.mQuery = query;
    }

    @Override
    public int getItemCount() {
        return mTabDataSetManager.getCount();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        mOnMoveItem.call(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        String query = mTabDataSetManager.getItem(position);
        mOnDeleteQuery.call(query);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements IItemTouchHelperViewHolder {

        @Bind(R.id.itemRoot)
        View mRoot;

        @Bind(R.id.itemTitle)
        AppCompatTextView mTitle;

        @Bind(R.id.itemDragHandle)
        ImageView mDragHandle;

        String mQuery;
        int mPosition;

        Action1<String> mOnDeleteQuery;

        public ViewHolder(View itemView, Action1<String> onDeleteQuery) {
            super(itemView);

            mOnDeleteQuery = onDeleteQuery;

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.itemDeleteButton)
        public void onDeleteClick() {
            mOnDeleteQuery.call(mQuery);
        }

        @OnTouch(R.id.itemDragHandle)
        public boolean onTouchDragHandle(MotionEvent event) {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(this);
            }
            return false;
        }

        public View getSwipableView() {
            return mRoot;
        }

        public String getQuery() {
            return mQuery;
        }

        @Override
        public void onItemSelected() {
            mRoot.setSelected(true);
        }

        @Override
        public void onItemClear() {
            mRoot.setSelected(false);
        }

        @OnClick(R.id.itemRoot)
        public void onRootClick() {
            mOnItemClick.call(mQuery);
        }
    }
}
