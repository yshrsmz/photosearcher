package net.yslibrary.photosearcher.ui.view;

import net.yslibrary.photosearcher.R;
import net.yslibrary.photosearcher.util.DrawableUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yshrsmz on 15/08/31.
 */
public class ClearableEditText extends AppCompatEditText
        implements View.OnFocusChangeListener, View.OnTouchListener {

    private Drawable mClearIcon;
    private boolean mHasFocus;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearIcon = DrawableUtil.getDrawable(getContext(), R.drawable.ic_close_black_24dp);
        mClearIcon.setBounds(
                0,
                0,
                mClearIcon.getIntrinsicWidth(),
                mClearIcon.getIntrinsicHeight());

        setOnTouchListener(this);
        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        mHasFocus = hasFocus;

        if (hasFocus) {
            setCompoundDrawables(
                    null,
                    null,
                    getText().toString().isEmpty() ? null : mClearIcon,
                    null);
        } else {
            setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (!mHasFocus) {
            return;
        }

        setCompoundDrawables(
                null,
                null,
                getText().toString().isEmpty() ? null : mClearIcon,
                null);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (getCompoundDrawables()[2] == null) {
            return false;
        }

        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }

        if (event.getX() > (getWidth() - getPaddingRight() - mClearIcon.getIntrinsicWidth())) {
            // clear text & remove icon
            setText("");
            this.setCompoundDrawables(null, null, null, null);
        }
        return false;
    }
}
