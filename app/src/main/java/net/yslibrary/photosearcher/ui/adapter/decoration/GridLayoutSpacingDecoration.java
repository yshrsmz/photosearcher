package net.yslibrary.photosearcher.ui.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yshrsmz on 15/08/26.
 */
public class GridLayoutSpacingDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridLayoutSpacingDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {

        outRect.left = mSpace;
        outRect.right = mSpace;
//        outRect.bottom = mSpace;
        outRect.top = mSpace;
    }
}