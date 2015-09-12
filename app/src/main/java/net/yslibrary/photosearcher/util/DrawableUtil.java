package net.yslibrary.photosearcher.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;

/**
 * Created by yshrsmz on 15/08/31.
 */
public class DrawableUtil {

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, @DrawableRes int drawableRes) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            return context.getResources().getDrawable(drawableRes, null);
        } else {
            return context.getResources().getDrawable(drawableRes);
        }
    }
}
