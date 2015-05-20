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
    private UIStateHelper uiStateHelper;
    private RefWatcher refWatcher;
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

    public RefWatcher getRefWatcher()
    {
        return refWatcher;
    }
    
    /**
     * Fragment创建时加入栈中
     * @param fragment
     */
    public void addFragment(BasicFragment fragment)
    {
        uiStateHelper.addFragment(fragment);
    }
    
    /**
     * Activity创建时加入栈中
     * @param activity
     */
    public void addActivity(BasicActivity activity)
    {
        uiStateHelper.addActivity(activity);
    }
    
    /**
     * Fragment移除时从栈中删除
     * @param fragment
     */
    public void removeFragment(BasicFragment fragment)
    {
        uiStateHelper.removeFragment(fragment);
    }
    
    /**
     * Activity移除时从栈中删除
     * @param activity
     */
    public void removeActivity(BasicActivity activity)
    {
        uiStateHelper.removeActivity(activity);
    }
    
    /**
     * 移除栈中的Activity
     * @param stackIndex
     */
    public void finishActivity(int stackIndex)
    {
        uiStateHelper.finishActivity(stackIndex);
    }

    /**
     * 关闭所有Activity
     */
    public void finishAll()
    {
        uiStateHelper.finishAll();
    }
    
    /**
     * 退出程序
     */
    public void exist(Context context)
    {
        finishAll();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        exist(this);
    }
}
