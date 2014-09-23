package com.android.baseline;

import android.app.Application;
import android.content.Context;

import com.android.baseline.framework.ui.BasicActivity;
import com.android.baseline.util.crash2email.GlobalExceptionHandler;

/**
 * 应用程序Appliccation
 * 
 * @author zhuhf
 * @version [SzMap, 2013-5-3]
 */
public class AppDroid extends Application
{
    private static AppDroid sInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler(getApplicationContext()));
    }

    /**
     * 退出程序
     */
    public void exist(Context context)
    {
        if (context instanceof BasicActivity)
        {
            ((BasicActivity) context).finishAll();
        }
    }

    public static AppDroid getInstance()
    {
        return sInstance;
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        exist(this);
    }
}
