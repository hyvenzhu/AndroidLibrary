package com.android.baseline;

import android.app.Application;
import android.content.Context;
import com.android.baseline.framework.ui.util.UIStateHelper;
import com.android.baseline.util.crash2email.GlobalExceptionHandler;

/**
 * App application
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-5-3]
 */
public class AppDroid extends Application
{
    private static AppDroid sInstance;
    public UIStateHelper uiStateHelper;
    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        uiStateHelper = new UIStateHelper();
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
