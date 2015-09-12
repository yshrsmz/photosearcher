package net.yslibrary.photosearcher.ui.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by yshrsmz on 15/08/31.
 */
public interface OnStartViewHolderDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
