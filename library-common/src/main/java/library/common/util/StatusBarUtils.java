package library.common.util;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jaeger.library.StatusBarUtil;

/**
 * 状态栏工具类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-11-21]
 */
public class StatusBarUtils {

    public static void setTranslucent(Activity activity, View offsetView) {
        StatusBarUtil.setTranslucentForImageView(activity, 0, offsetView);
    }

    public static void setTranslucent(Fragment fragment, View offsetView) {
        StatusBarUtil.setTranslucentForImageViewInFragment(fragment.getActivity(), 0, offsetView);
    }

    public static void setColor(Activity activity, @ColorInt int color) {
        StatusBarUtil.setColor(activity, color, 0);
    }

    public static void setLightMode(Activity activity) {
        StatusBarUtil.setLightMode(activity);
    }

    public static void setDarkMode(Activity activity) {
        StatusBarUtil.setDarkMode(activity);
    }
}
