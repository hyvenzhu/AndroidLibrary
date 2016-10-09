package com.android.baseline.framework.ui.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.android.baseline.AppDroid;
import com.android.baseline.R;
import com.android.baseline.framework.ui.activity.base.BaseFragment;
import com.android.baseline.framework.ui.activity.base.UIInterface;
import com.android.baseline.framework.ui.view.LoadingView;
/**
 * 基类Fragment
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-10-27]
 */
public class BasicFragment extends BaseFragment
{
    /** 当前Fragment是否处于暂停状态*/
    protected boolean isPaused = true;
    /** 加载进度*/
    private LoadingView mLoadingView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppDroid.getInstance().uiStateHelper.addFragment(this);
    }
    
    /**
     * view的初始化等操作
     */
    protected void afterSetContentView(View view)
    {
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        if (mLoadingView != null)
        {
            mLoadingView.register(this);
        }
    }
    
    /**
     * 正在加载
     */
    protected void onLoading()
    {
        onLoading(R.string.app_name);
    }
    
    /**
     * 正在加载
     * @param obj
     */
    protected void onLoading(Object obj)
    {
        onLoading(R.string.app_name, obj);
    }
    
    /**
     * 正在加载
     * @param stringId 描述信息
     */
    protected void onLoading(int stringId)
    {
        onLoading(getResources().getString(stringId));
    }
    
    /**
     * 正在加载
     * @param stringId 描述信息
     * @param obj
     */
    public void onLoading(int stringId, Object obj)
    {
        onLoading(getResources().getString(stringId), obj);
    }
    
    /**
     * 正在加载
     * @param loadDesc 描述信息
     */
    protected void onLoading(String loadDesc)
    {
        mLoadingView.onLoading(loadDesc, null);
    }
    
    /**
     * 正在加载
     * @param loadDesc 描述信息
     * @param obj 传递的参数
     */
    public void onLoading(String loadDesc, Object obj)
    {
        mLoadingView.onLoading(loadDesc, obj);
    }
    
    /**
     * 失败
     */
    protected void onFailure()
    {
        onFailure(R.string.loading_failure);
    }
    
    /**
     * 失败
     * @param stringId 描述信息
     */
    protected void onFailure(int stringId)
    {
        onFailure(getResources().getString(stringId));
    }
    
    /**
     * 失败
     * @param errorDesc 描述信息
     */
    protected void onFailure(String errorDesc)
    {
        mLoadingView.onFailure(errorDesc);
    }
    
    /**
     * 成功
     */
    protected void onSuccess()
    {
        mLoadingView.onSuccess();
    }
    
    private UIInterface uiInterface;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof BasicActivity))
        {
            throw new RuntimeException("Activity must implements Interface 'UIInterface'.");
        }
        uiInterface = (UIInterface)context;
    }

    /**
     * 根据字符串 show toast<BR>
     * @param message 字符串
     */
    public void showToast(CharSequence message)
    {
        if (!isVisible())
        {
            return;
        }
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
        AppDroid.getInstance().uiStateHelper.removeFragment(this);
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
    @Override
    public void onResponse(Message msg)
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
    
    protected boolean checkResponse(Message msg)
    {
        return checkResponse(msg, null, null, true);
    }
    
    protected boolean checkResponse(Message msg, boolean tipError)
    {
        return checkResponse(msg, null, null, tipError);
    }
    
    protected boolean checkResponse(Message msg, String errorTip)
    {
        return checkResponse(msg, null, errorTip, true);
    }
    
    protected boolean checkResponse(Message msg, String successTip, String errorTip)
    {
        return checkResponse(msg, successTip, errorTip, true);
    }
    
    /**
     * 校验服务器响应结果
     * @param msg
     * @param successTip 成功提示
     * @param errorTip 失败提示    为空使用服务器信息或本地固定信息
     * @param tipError 是否提示错误信息
     * @return true 业务成功, false业务失败
     */
    protected boolean checkResponse(Message msg, String successTip, String errorTip, boolean tipError)
    {
        return ((BasicActivity)uiInterface).checkResponse(msg, successTip, errorTip, tipError);
    }
}
