package net.yslibrary.photosearcher.ui.listener;

import android.support.v4.view.ViewPager;

/**
 * Created by yshrsmz on 15/08/31.
 */
public abstract class ViewPagerOnPageSelectedListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // empty
    }

    @Override
    public abstract void onPageSelected(int position);

    @Override
    public void onPageScrollStateChanged(int state) {
        // empty
    }
}
