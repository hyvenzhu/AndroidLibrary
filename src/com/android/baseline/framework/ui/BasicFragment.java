package com.android.baseline.framework.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

import com.android.baseline.AppDroid;
import com.android.baseline.framework.ui.base.UIInterface;
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
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppDroid.getInstance().addFragment(this);
    }
    
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
        // 屏蔽Fragment布局的点击事件, 否则事件可能会被“栈”下面的Fragment捕获
        interceptTouchEvent(v, true);
        ViewUtils.inject(fragment, v);
        afterSetContentView(v);
        return v;
    }
    
    /**
     * setContentView之后调用, 进行view的初始化等操作
     */
    private void afterSetContentView(View v)
    {
        init(v);
    }
    
    /**
     * 不希望使用默认的注解来初始化View
     */
    protected void init(View v)
    {
    }
    
    /**
     * Fragment布局是否拦截事件
     * @param interceptEvent true拦截|false不拦截
     */
    public void interceptTouchEvent(boolean interceptEvent)
    {
        interceptTouchEvent(getView(), interceptEvent);
    }
    
    /**
     * Fragment布局是否拦截事件
     * @param view
     * @param interceptEvent true拦截|false不拦截
     */
    private void interceptTouchEvent(View view, boolean interceptEvent)
    {
        if (interceptEvent)
        {
            if (view != null)
            {
                view.setOnTouchListener(new OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        return true;
                    }
                });
            }
        }
        else
        {
            if (view != null)
            {
                view.setOnTouchListener(null);
            }
        }
    }
    
    private UIInterface uiInterface;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (!(activity instanceof BasicActivity))
        {
            throw new RuntimeException("Activity must implements Interface 'UIInterface'.");
        }
        uiInterface = (UIInterface)activity;
    }
    
    /**
     * 根据字符串 show toast<BR>
     * @param message 字符串
     */
    public void showToast(CharSequence message)
    {
        uiInterface.showToast(message);
    }

    public void showProgress(String message)
    {
        showProgress(message, true);
    }

    public void showProgress(String message, boolean cancelable)
    {
        uiInterface.showProgress(message, cancelable);
    }
    
    public void hideProgress()
    {
        uiInterface.hideProgress();
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
        uiInterface.hideProgress();
        AppDroid.getInstance().removeFragment(this);
    }
    
    /**
     * 关闭当前Fragment
     */
    protected void finish()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();
        getFragmentManager().popBackStackImmediate();
    }
    
    /**
     * 事件分发
     * @param msg
     */
    protected void onResponse(Message msg)
    {
        if (dialogHidden)
        {
            uiInterface.hideProgress();
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
