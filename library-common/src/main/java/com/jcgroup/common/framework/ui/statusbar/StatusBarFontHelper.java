package com.jcgroup.common.framework.ui.statusbar;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.IntDef;

import com.jcgroup.common.framework.ui.statusbar.impl.AndroidMStatusBar;
import com.jcgroup.common.framework.ui.statusbar.impl.FlymeStatusBar;
import com.jcgroup.common.framework.ui.statusbar.impl.MIUIStatusBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 状态栏字体颜色 Helper
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public class StatusBarFontHelper {
    @IntDef({
            OTHER,
            MIUI,
            FLYME,
            ANDROID_M
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SystemType {

    }

    public static final int OTHER = -1;
    public static final int MIUI = 1;
    public static final int FLYME = 2;
    public static final int ANDROID_M = 3;


    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUI、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param isFontColorDark
     * @return 1:MIUI 2:Flyme 3:android6.0
     */
    public static int setStatusBarMode(Activity activity, boolean isFontColorDark) {
        @SystemType int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (new MIUIStatusBar().setStatusBarLightMode(activity, isFontColorDark)) {
                result = MIUI;
            } else if (new FlymeStatusBar().setStatusBarLightMode(activity, isFontColorDark)) {
                result = FLYME;
            } else if (new AndroidMStatusBar().setStatusBarLightMode(activity, isFontColorDark)) {
                result = ANDROID_M;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUI6、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type 1:MIUI 2:Flyme 3:android6.0
     */
    public static void setLightMode(Activity activity, @SystemType int type) {
        setStatusBarMode(activity, type, true);

    }

    private static void setStatusBarMode(Activity activity, @SystemType int type, boolean isFontColorDark) {
        if (type == MIUI) {
            new MIUIStatusBar().setStatusBarLightMode(activity, isFontColorDark);
        } else if (type == FLYME) {
            new FlymeStatusBar().setStatusBarLightMode(activity, isFontColorDark);
        } else if (type == ANDROID_M) {
            new AndroidMStatusBar().setStatusBarLightMode(activity, isFontColorDark);
        }
    }

}
