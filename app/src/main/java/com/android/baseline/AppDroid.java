package com.android.baseline;

import android.app.Application;
import android.content.Context;

import com.android.baseline.framework.ui.BasicActivity;
import com.android.baseline.framework.ui.BasicFragment;
import com.android.baseline.framework.ui.util.UIStateHelper;
import com.android.baseline.util.crash2email.GlobalExceptionHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * App application
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-5-3]
 */
public class AppDroid extends Application
{
    private static AppDroid sInstance;
    public UIStateHelper uiStateHelper;
    public RefWatcher refWatcher;
    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        uiStateHelper = new UIStateHelper();
        refWatcher = LeakCanary.install(this);
    }

    public static AppDroid getInstance()
    {
        return sInstance;
    }

    /**
     * 退出程序
     */
    public void exist(Context context)
    {
        uiStateHelper.finishAll();
    }
}
