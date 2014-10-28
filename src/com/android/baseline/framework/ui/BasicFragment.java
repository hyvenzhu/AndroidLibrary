package com.android.baseline.framework.ui;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.baseline.framework.ui.BasicActivity;
import com.android.baseline.framework.ui.base.annotations.ViewUtils;
/**
 * 基类Fragment
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-10-27]
 */
public abstract class BasicFragment extends Fragment
{
    /** 当前Fragment是否处于暂停状态*/
    protected boolean isPaused = true;
    /**
     * 从资源加载View
     * @param inflater
     * @param container
     * @param resourceId
     * @param fragment
     * @return
     */
    protected View inflate(LayoutInflater inflater, ViewGroup container, int resourceId, BasicFragment fragment)
    {
        View v = inflater.inflate(resourceId, container, false);
        ViewUtils.inject(fragment, v);
        return v;
    }
    
    private BasicActivity basicActivity;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (!(activity instanceof BasicActivity))
        {
            throw new RuntimeException("Activity must extend BasicActivity.");
        }
        basicActivity = (BasicActivity)activity;
    }
    
    /**
     * 根据字符串 show toast<BR>
     * @param message 字符串
     */
    public void showToast(CharSequence message)
    {
        basicActivity.showToast(message);
    }

    public void showProgress(String message)
    {
        showProgress(message, true);
    }

    public void showProgress(String message, boolean cancelable)
    {
        basicActivity.showProgress(message, cancelable);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        isPaused = false;
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        isPaused = true;
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        basicActivity.hideProgress();
    }
    
    /**
     * 事件分发
     * @param msg
     */
    protected void onResponse(Message msg)
    {
        if (dialogHidden)
        {
            basicActivity.hideProgress();
        }
    }

    boolean dialogHidden = true;

    /**
     * 设置网络请求结束是否关闭对话框, 默认是关闭
     * 
     * @param hidden true关闭 false不关闭
     */
    protected void defaultDialogHidden(boolean hidden)
    {
        dialogHidden = hidden;
    }
    
    /**
     * EventBus订阅者事件通知的函数, UI线程
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
        onResponse(msg);
    }
}
