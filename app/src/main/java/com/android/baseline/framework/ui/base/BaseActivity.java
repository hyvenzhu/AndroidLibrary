package com.android.baseline.framework.ui.base;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.android.baseline.framework.logic.BaseLogic;
import com.android.baseline.framework.logic.ILogic;
import com.android.baseline.framework.ui.base.annotations.ViewUtils;
import com.android.baseline.framework.ui.util.ToolBarHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类Activity, 提供业务逻辑的处理和深层的UI处理 
 * 1、实现View初始化、事件绑定 
 * 2、提供EventBus事件通知
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private ToolBarHelper mToolBarHelper ;
    public Toolbar toolbar ;
    private boolean isDestroyed; // Activity是否已销毁
    @Override
    public void setContentView(int layoutResID)
    {
        mToolBarHelper = new ToolBarHelper(this, layoutResID) ;
        toolbar = mToolBarHelper.getToolBar() ;
        super.setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar) ;
        /*View注解*/
        ViewUtils.inject(this);

        afterSetContentView();
    }

    /**
     * ToolBar自定义
     * @param toolbar
     */
    protected void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0, 0);
    }

    /**
     * 初始化一些View
     */
    protected void afterSetContentView()
    {

    }

    private List<BaseLogic> logics = new ArrayList<BaseLogic>(); // 存储BaseLogic
    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     * @param logic
     * @param <T>
     * @return
     */
    protected <T extends BaseLogic> T registeLogic(BaseLogic logic)
    {
        logics.add(logic);
        return (T)logic;
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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        isDestroyed = true;
        for(BaseLogic logic : logics)
        {
            unregister(logic);
        }
    }

    /**
     * EventBus订阅者事件通知的函数, UI线程
     * 
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
        if (!isDestroyed && !isFinishing())
        {
            onResponse(msg);
        }
    }
    
    public abstract void onResponse(Message msg);
}
