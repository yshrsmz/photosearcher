package net.yslibrary.photosearcher.ui.adapter;

import net.yslibrary.photosearcher.model.ITabDataSetManager;
import net.yslibrary.photosearcher.ui.fragment.SearchTimelineFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;


/**
 * Created by yshrsmz on 15/08/29.
 */
public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    ITabDataSetManager<String> mDataSetManager;

    public SearchPagerAdapter(FragmentManager fm,
                              ITabDataSetManager<String> dataSetManager) {
        super(fm);
        mDataSetManager = dataSetManager;
    }

    @Override
    public Fragment getItem(int position) {
        String query = mDataSetManager.getItem(position);

        return SearchTimelineFragment.newInstance(query);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mDataSetManager.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataSetManager.getItem(position);
    }
}
