package com.android.baseline.framework.ui.activity;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * 维护Activity和Fragment状态
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-12-15]
 */
public class UIStateHelper {
    /**
     * 保存栈中的Activity
     */
    private List<AppCompatActivity> activityStack = new ArrayList<>();
    /**
     * 保存FragmentTransation中的Fragment
     */
    private List<Fragment> fragmentStack = new ArrayList<Fragment>();

    /**
     * Fragment创建时加入栈中
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        fragmentStack.add(fragment);
    }

    /**
     * Activity创建时加入栈中
     *
     * @param activity
     */
    public void addActivity(AppCompatActivity activity) {
        activityStack.add(activity);
    }

    /**
     * Fragment移除时从栈中删除
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        fragmentStack.remove(fragment);
    }

    /**
     * Activity移除时从栈中删除
     *
     * @param activity
     */
    public void removeActivity(AppCompatActivity activity) {
        activityStack.remove(activity);
    }

    /**
     * 关闭所有Activity
     */
    public void finishAll() {
        for (AppCompatActivity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 返回栈顶Activity
     * @return
     */
    public AppCompatActivity getTopActivity() {
        int size = activityStack.size();
        return size > 0? activityStack.get(size - 1) : null;
    }
}
