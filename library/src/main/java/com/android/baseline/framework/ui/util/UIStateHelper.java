package com.android.baseline.framework.ui.util;

import java.util.ArrayList;
import java.util.List;

import com.android.baseline.framework.ui.BasicActivity;
import com.android.baseline.framework.ui.BasicFragment;

import android.app.Activity;
/**
 * 维护Activity和Fragment状态
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-12-15]
 */
public class UIStateHelper
{
    /** 保存栈中的Activity */
    private List<Activity> activityStack = new ArrayList<Activity>();
    /** 保存FragmentTransation中的Fragment */
    private List<BasicFragment> fragmentStack = new ArrayList<BasicFragment>();
    
    /**
     * Fragment创建时加入栈中
     * @param fragment
     */
    public void addFragment(BasicFragment fragment)
    {
        fragmentStack.add(fragment);
    }
    
    /**
     * Activity创建时加入栈中
     * @param activity
     */
    public void addActivity(BasicActivity activity)
    {
        activityStack.add(activity);
    }
    
    /**
     * Fragment移除时从栈中删除
     * @param fragment
     */
    public void removeFragment(BasicFragment fragment)
    {
        fragmentStack.remove(fragment);
    }
    
    /**
     * Activity移除时从栈中删除
     * @param activity
     */
    public void removeActivity(BasicActivity activity)
    {
        activityStack.remove(activity);
    }
    
    /**
     * 关闭所有Activity
     */
    public void finishAll()
    {
        for (Activity activity : activityStack)
        {
            activity.finish();
        }
        activityStack.clear();
    }
}
