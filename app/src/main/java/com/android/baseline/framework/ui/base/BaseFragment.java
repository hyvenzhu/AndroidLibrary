package com.android.baseline.framework.ui.base;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.logic.ILogic;
/**
 * 基类Fragment, 提供业务逻辑的处理和深层的UI处理 
 * 1、提供EventBus事件通知
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-4-1]
 */
public abstract class BaseFragment extends Fragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
        {
            onRestoreState(savedInstanceState);
        }
    }
    
    /**
     * 恢复临时状态
     * @param savedState
     */
    protected void onRestoreState(Bundle savedState)
    {
        Logger.d("BaseFragment", "onRestoreState >>>" + savedState);
    }

    /**
     * 解绑当前订阅者
     * @param iLogics
     */
    protected void unregister(ILogic... iLogics)
    {
        for(ILogic iLogic : iLogics)
        {
            if (iLogic != null)
            {
                iLogic.cancelAll();
                iLogic.unregister(this);
            }
        }
    }

    /**
     * 解绑所有订阅者
     */
    protected void unregisterAll(ILogic... iLogics)
    {
        for(ILogic iLogic : iLogics)
        {
            if (iLogic != null)
            {
                iLogic.cancelAll();
                iLogic.unregisterAll();
            }
        }
    }
    
    /**
     * EventBus订阅者事件通知的函数, UI线程
     * 
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
        if (isAdded() && !isDetached() && !isRemoving())
        {
            onResponse(msg);
        }
    }
    
    protected abstract void onResponse(Message msg);
}
