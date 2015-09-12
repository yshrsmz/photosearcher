package net.yslibrary.photosearcher.ui.view;

import net.yslibrary.photosearcher.R;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * Created by yshrsmz on 15/08/30.
 */
public class LoadingSpinner {

    private View mSpinner;

    private ViewGroup mParent;

    private boolean mIsShowing;

    private boolean mIsInoperable = false;

    public LoadingSpinner() {
        // empty constructor
    }

    /**
     * Show loading spinner on the Activity
     *
     * @param activity target Activity
     */
    public void show(Activity activity) {
        if (mIsShowing) {
            return;
        }

        mParent = ButterKnife.findById(activity, android.R.id.content);
        mSpinner = LayoutInflater.from(activity).inflate(R.layout.component_loading_spinner, null);
        addSpinner();
    }

    public void show(Fragment fragment) {
        if (mIsShowing) {
            return;
        }

        mParent = (ViewGroup) fragment.getView();
        mSpinner = LayoutInflater.from(fragment.getActivity())
                .inflate(R.layout.component_loading_spinner, null);
        addSpinner();
    }

    /**
     * Remove spinner from view
     */
    public void dismiss() {
        if (mIsShowing && mParent != null) {
            mParent.removeView(mSpinner);
            mIsShowing = false;
        }
    }

    public boolean isShown() {
        return mIsShowing;
    }

    private void addSpinner() {
        mSpinner.setClickable(false);

        ProgressBar progressBar = ButterKnife.findById(mSpinner, R.id.loadingSpinner);
        progressBar.getIndeterminateDrawable().setColorFilter(
                progressBar.getContext().getResources().getColor(R.color.accent_color),
                PorterDuff.Mode.SRC_IN);

        if (mParent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mParent.addView(mSpinner, lp);
        } else {
            mParent.addView(mSpinner);
        }
        mIsShowing = true;

    }

    public interface LoadingSpinnerListener {

        void toggleLoadingSpinner(boolean show);

        boolean isShown();
    }
}
