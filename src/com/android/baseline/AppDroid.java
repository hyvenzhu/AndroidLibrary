package com.android.baseline;

import android.app.Application;
import android.content.Context;

import com.android.baseline.framework.ui.BasicActivity;
import com.android.baseline.util.anrwatchdog.ANRWatchDog;
import com.android.baseline.util.crash2email.GlobalExceptionHandler;

/**
 * App application
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-5-3]
 */
public class AppDroid extends Application
{
    private static AppDroid sInstance;
    private ANRWatchDog anrWatchDog;
    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        // ANR Watch support
        if (BuildConfig.DEBUG == false)
        {
            anrWatchDog = new ANRWatchDog(10000);
            anrWatchDog.start();
        }
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
        anrWatchDog.interrupt();
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
