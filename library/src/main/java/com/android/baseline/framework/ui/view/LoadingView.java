package com.android.baseline.framework.ui.view;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baseline.R;

import org.greenrobot.eventbus.EventBus;

/**
 * 加载数据View, 提供加载提示、失败点击重试等功能
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-1-28]
 */
public class LoadingView extends RelativeLayout implements OnClickListener
{
    private ProgressBar progressBar;
    private TextView tipTxt;
    private boolean isLoading = true;
    private EventBus eventBus;
    private Object mSubcriber;
    public LoadingView(Context context)
    {
        super(context);
        init();
    }
    
    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    
    public LoadingView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init()
    {
        // 当前View的点击事件
        setOnClickListener(this);
        eventBus = new EventBus();
    }
    
    /**
     * 注册观察者
     * @param subcriber
     */
    public void register(Object subcriber)
    {
        mSubcriber = subcriber;
        eventBus.register(mSubcriber);
    }
    
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        progressBar = (ProgressBar)findViewById(R.id.loading_progressBar);
        tipTxt = (TextView)findViewById(R.id.tip_txt);
        onLoading();
    }
    
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (mSubcriber != null)
        {
            eventBus.unregister(mSubcriber);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        if (!isLoading)
        {
            onLoading();
        }
    }
    
    /**
     * 广播事件, 包括“正在加载”等
     * @param eventId
     * @param obj
     */
    private void postEvent(int eventId, Object obj)
    {
        Message msg = new Message();
        msg.what = eventId;
        msg.obj = obj;
        eventBus.post(msg);
    }
    
    /**
     * 正在加载
     */
    public void onLoading()
    {
        onLoading(R.string.app_name);
    }
    
    /**
     * 正在加载
     * @param obj
     */
    public void onLoading(Object obj)
    {
        onLoading(R.string.app_name, obj);
    }
    
    /**
     * 正在加载
     * @param stringId 描述信息
     */
    public void onLoading(int stringId)
    {
        onLoading(stringId, null);
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
    public void onLoading(String loadDesc)
    {
        onLoading(loadDesc, null);
    }
    
    /**
     * 正在加载
     * @param loadDesc 描述信息
     * @param obj 传递的参数
     */
    public void onLoading(String loadDesc, Object obj)
    {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        tipTxt.setText(loadDesc);
        tipTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        // 发送广播通知注册者
        postEvent(R.id.onLoading, obj);
    }
    
    /**
     * 失败
     */
    public void onFailure()
    {
        onFailure(R.string.loading_failure);
    }
    
    /**
     * 失败
     * @param stringId 描述信息
     */
    public void onFailure(int stringId)
    {
        onFailure(getResources().getString(stringId));
    }
    
    /**
     * 失败
     * @param errorDesc 描述信息
     */
    public void onFailure(String errorDesc)
    {
        isLoading = false;
        progressBar.setVisibility(View.GONE);
        tipTxt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.loading_error_tip), null, null, null);
        tipTxt.setText(errorDesc);
        setVisibility(View.VISIBLE);
    }
    
    /**
     * 成功
     */
    public void onSuccess()
    {
        isLoading = false;
        setVisibility(View.GONE);
    }
}
