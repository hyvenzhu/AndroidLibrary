package com.android.baseline.framework.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.baseline.AppDroid;
import com.android.baseline.R;
import com.android.baseline.framework.ui.base.BaseFragment;
import com.android.baseline.framework.ui.base.UIInterface;
import com.android.baseline.framework.ui.base.annotations.ViewUtils;
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
    private View mView;
    private Fragment mFragment;
    
    /** 标题栏 */
    protected View titleLay;
    protected Button leftBtn;
    protected TextView titleTxt;
    protected Button rightBtn;
    
    /** 加载进度*/
    private LoadingView mLoadingView;
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
    protected View inflate(LayoutInflater inflater, ViewGroup container, int resourceId, Fragment fragment)
    {
        mView = inflater.inflate(resourceId, container, false);
        mFragment = fragment;
        return mView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // 屏蔽Fragment布局的点击事件, 否则事件可能会被“栈”下面的Fragment捕获
        interceptTouchEvent(mView, true);
        ViewUtils.inject(mFragment, mView);
        afterSetContentView();
    }
    
    /**
     * setContentView之后调用, 进行view的初始化等操作
     */
    private void afterSetContentView()
    {
        init(mView);
        if (mLoadingView != null)
        {
            mLoadingView.register(this);
        }
    }
    
    /**
     * 不希望使用默认的注解来初始化View
     */
    protected void init(View v)
    {
        titleLay = v.findViewById(R.id.title_lay);
        leftBtn = (Button)v.findViewById(R.id.title_left_btn);
        titleTxt = (TextView)v.findViewById(R.id.title_txt);
        rightBtn = (Button)v.findViewById(R.id.title_right_btn);
        mLoadingView = (LoadingView)v.findViewById(R.id.loading_view);
    }
    
    /**
     * 设置标题栏属性
     * @param leftVisible 左侧按钮是否可见
     * @param resId 标题资源id
     * @param rightVisible 右侧按钮是否可见
     */
    protected void setTitleBar(boolean leftVisible, int resId, boolean rightVisible)
    {
        leftBtn.setVisibility(leftVisible? View.VISIBLE : View.INVISIBLE);
        titleTxt.setText(resId);
        rightBtn.setVisibility(rightVisible? View.VISIBLE : View.INVISIBLE);
    }
    
    /**
     * 设置标题栏属性
     * @param leftVisible 左侧按钮是否可见
     * @param title 标题
     * @param rightVisible 右侧按钮是否可见
     */
    protected void setTitleBar(boolean leftVisible, String title, boolean rightVisible)
    {
        leftBtn.setVisibility(leftVisible? View.VISIBLE : View.INVISIBLE);
        titleTxt.setText(title);
        rightBtn.setVisibility(rightVisible? View.VISIBLE : View.INVISIBLE);
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
    
    /**
     * Fragment布局是否拦截事件
     * @param interceptEvent true拦截|false不拦截
     */
    protected void interceptTouchEvent(boolean interceptEvent)
    {
        interceptTouchEvent(mView, interceptEvent);
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
