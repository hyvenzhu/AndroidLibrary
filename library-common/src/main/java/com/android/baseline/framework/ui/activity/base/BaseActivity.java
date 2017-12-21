package com.android.baseline.framework.ui.activity.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.baseline.AppDroid;
import com.android.baseline.framework.logic.EventLogic;
import com.android.baseline.framework.task.Task;
import com.android.baseline.framework.ui.activity.base.helper.LogicHelper;
import com.android.baseline.framework.ui.activity.base.helper.TaskHelper;
import com.android.baseline.framework.ui.activity.presenter.ActivityPresenter;
import com.android.baseline.framework.ui.activity.view.AppDelegate;
import com.android.baseline.framework.ui.activity.view.IDelegate;
import com.android.baseline.framework.ui.statusbar.StatusBarFontHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 基类Activity, 提供业务逻辑的处理
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public abstract class BaseActivity extends ActivityPresenter {
    
    /**
     * 状态栏背景色透明
     */
    private void makeStatusBarTransparent() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // >=5.0
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // >=4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
