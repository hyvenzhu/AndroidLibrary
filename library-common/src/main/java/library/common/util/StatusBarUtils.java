package library.common.util;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.view.View;

import com.jaeger.library.StatusBarUtil;

/**
 * 状态栏工具类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-11-21]
 */
public class StatusBarUtils {

    public static void transparentWithOffsetInActivity(Activity activity, View offsetView) {
        StatusBarUtil.setTranslucentForImageView(activity, 0, offsetView);
    }

    public static void transparentWithOffsetInFragment(Activity activity, View offsetView) {
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, offsetView);
    }

    /**
     * 状态栏着色，会使得 window 位于状态栏之下（如果使用滑动返回，阴影效果无法延展到状态栏）
     *
     * @param activity
     * @param color
     */
    public static void setColor(Activity activity, @ColorInt int color) {
        StatusBarUtil.setColor(activity, color, 0);
    }

    public static void setLightMode(Activity activity, boolean lightMode) {
        if (lightMode) {
            StatusBarUtil.setLightMode(activity);
        } else {
            StatusBarUtil.setDarkMode(activity);
        }
    }
}
