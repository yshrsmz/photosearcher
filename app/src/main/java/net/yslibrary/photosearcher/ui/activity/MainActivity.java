package net.yslibrary.photosearcher.ui.activity;

import com.squareup.otto.Bus;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
import net.yslibrary.photosearcher.App;
import net.yslibrary.photosearcher.R;
import net.yslibrary.photosearcher.event.SearchTimelineScrollEvent;
import net.yslibrary.photosearcher.graph.component.DaggerMainComponent;
import net.yslibrary.photosearcher.graph.component.MainComponent;
import net.yslibrary.photosearcher.graph.module.ActivityModule;
import net.yslibrary.photosearcher.model.TimelineManager;
import net.yslibrary.photosearcher.ui.adapter.OnStartViewHolderDragListener;
import net.yslibrary.photosearcher.ui.adapter.SearchPagerAdapter;
import net.yslibrary.photosearcher.ui.adapter.TabListAdapter;
import net.yslibrary.photosearcher.ui.adapter.TabListTouchHelperCallback;
import net.yslibrary.photosearcher.ui.listener.ViewPagerOnPageSelectedListener;
import net.yslibrary.photosearcher.util.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yshrsmz on 15/08/25.
 */
public class MainActivity extends AppCompatActivity
        implements OnStartViewHolderDragListener, TabLayout.OnTabSelectedListener {

    @Bind(R.id.itemTab)
    TabLayout mTabLayout;

    @Bind(R.id.itemPager)
    ViewPager mViewPager;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.tabDrawer)
    LinearLayout mTabDrawer;

    @Bind(R.id.tabList)
    RecyclerView mTabList;

    @Bind(R.id.itemNewQueryField)
    AppCompatEditText mNewQueryField;

    @Bind(R.id.emptyText)
    AppCompatTextView mEmptyText;

    @Inject
    TimelineManager mTimelineManager;

    @Inject
    Bus mBus;

    private MainComponent mComponent;
    private SearchPagerAdapter mPagerAdapter;
    private TabListAdapter mTabListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ItemTouchHelper mItemTouchHelper;

    private int mCurrentTabPosition = 0;

    private Handler mHandler = new Handler();

    // swipe & drag callback for tab management drawer
    private TabListTouchHelperCallback mTabListTouchCallback;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        return intent;
    }

    public MainComponent getComponent() {
        if (mComponent == null) {
            mComponent = DaggerMainComponent.builder()
                    .appComponent(App.get(this).getComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }

        return mComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getComponent().inject(this);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        setupPager();
        setupRightDrawer();
        updateEmptyState();

        mDrawerToggle.syncState();
    }

    private void setupPager() {
        mPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), mTimelineManager);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPagerOnPageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentTabPosition = position;
            }
        });

        refreshTabLayout(true);
    }

    private void refreshTabLayout() {
        refreshTabLayout(false);
    }

    private void refreshTabLayout(boolean setViewPagerListener) {
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        if (setViewPagerListener) {
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        }
        if (mPagerAdapter.getCount() > 0) {
            int curItem = mViewPager.getCurrentItem();
            if (mTabLayout.getSelectedTabPosition() != curItem) {
                mTabLayout.getTabAt(curItem).select();
            }
        }

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setOnTabSelectedListener(this);
    }

    private void setupRightDrawer() {
        mTabListAdapter = new TabListAdapter(
                mTimelineManager,
                this,
                this::onDeleteQuery,
                this::onMoveTab,
                this::onTabSelect);

        mTabListTouchCallback = new TabListTouchHelperCallback(mTabListAdapter);

        mTabList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTabList.setHasFixedSize(true);
        mTabList.setAdapter(mTabListAdapter);

        mItemTouchHelper = new ItemTouchHelper(mTabListTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mTabList);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                UIUtil.hideKeyboard(MainActivity.this);

                int newPosition = mCurrentTabPosition;
                mDrawerToggle.syncState();

                mHandler.postDelayed(() -> mViewPager.setCurrentItem(newPosition, false), 100);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void updateEmptyState() {
        int visibility = View.VISIBLE;
        if (mTimelineManager.getCount() > 0) {
            visibility = View.GONE;
        }
        mEmptyText.setVisibility(visibility);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // workaround for CoordinatorLayout NPE
        // https://code.google.com/p/android/issues/detail?id=183166
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionManageTabs) {
            mDrawerLayout.openDrawer(mTabDrawer);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.itemAddQueryButton)
    public void onAddNewQuery() {
        if (StringUtil.isNotEmpty(mNewQueryField.getText())) {
            mTimelineManager.addTimeline(StringUtil.trim(mNewQueryField.getText().toString()));
            mTabListAdapter.notifyDataSetChanged();
            mPagerAdapter.notifyDataSetChanged();
            refreshTabLayout();
            updateEmptyState();
            mNewQueryField.getText().clear();
        }
    }

    public void onTabSelect(String query) {
        int position = mTimelineManager.getQueries().indexOf(query);
        mCurrentTabPosition = position;
        mDrawerLayout.closeDrawer(mTabDrawer);
    }


    /**
     * delete selected query & update tabs
     *
     * @param query the query to delete
     */
    private void onDeleteQuery(String query) {
        mTimelineManager.deleteTimeline(query);
        mTabListAdapter.notifyDataSetChanged();
        mTabListAdapter.notifyDataSetChanged();
        mPagerAdapter.notifyDataSetChanged();
        refreshTabLayout();
        updateEmptyState();
    }

    private void onMoveTab(Integer from, Integer to) {
        mTimelineManager.moveItem(from, to);
        mTabListAdapter.notifyItemMoved(from, to);
        mPagerAdapter.notifyDataSetChanged();
        refreshTabLayout();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mTabDrawer)) {
            mDrawerLayout.closeDrawer(mTabDrawer);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // workaround for `onTabSelected` and `onTabReselected` will trigger at the same time
        // https://code.google.com/p/android/issues/detail?id=177189
        mTabLayout.setOnTabSelectedListener(null);
        mViewPager.setCurrentItem(tab.getPosition());
        mTabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        String query = mTimelineManager.getItem(position);
        mBus.post(new SearchTimelineScrollEvent(query, 0));
    }
}
