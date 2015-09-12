package net.yslibrary.photosearcher.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by yshrsmz on 15/08/30.
 */
public class ViewUtil {

    public static Float sDensity;

    public static int dpToPx(Context context, int dp) {
        return (int) (getDensity(context) * (float) dp + 0.5f);
    }

    public static float getDensity(Context context) {
        if (sDensity == null) {
            DisplayMetrics metrics = getDisplayMetrics(context);
            sDensity = metrics.density;
        }
        return sDensity;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
