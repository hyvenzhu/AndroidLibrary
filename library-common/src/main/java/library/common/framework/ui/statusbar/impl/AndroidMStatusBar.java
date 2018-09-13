package library.common.framework.ui.statusbar.impl;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import library.common.framework.ui.statusbar.IStatusBar;

/**
 * 6.0状态栏字体颜色
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class AndroidMStatusBar implements IStatusBar {
    /**
     * @param activity
     * @param isFontColorDark
     * @return if version is lager than M
     */
    @Override
    public boolean setStatusBarLightMode(Activity activity, boolean isFontColorDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isFontColorDark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            return true;
        }
        return false;
    }

}
