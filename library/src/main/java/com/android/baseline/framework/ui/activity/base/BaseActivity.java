package com.android.baseline.framework.ui.activity.base;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.baseline.framework.logic.EventLogic;
import com.android.baseline.framework.task.Task;
import com.android.baseline.framework.ui.activity.annotations.ViewUtils;
import com.android.baseline.framework.ui.activity.base.helper.ContentViewHelper;
import com.android.baseline.framework.ui.activity.base.helper.KeyboardUtil;
import com.android.baseline.framework.ui.activity.base.helper.LogicHelper;
import com.android.baseline.framework.ui.activity.base.helper.TaskHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 基类Activity, 提供业务逻辑的处理和深层的UI处理
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private boolean isDestroyed; // Activity是否已销毁

    KeyboardUtil keyboardUtil;
    @Override
    public void setContentView(int layoutResID) {
        ContentViewHelper mToolBarHelper = new ContentViewHelper(this, layoutResID, defaultTitleBarVisible());
        toolbar = mToolBarHelper.getToolBar();
        super.setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar);
        /*View注解*/
        ViewUtils.inject(this);

        afterSetContentView();

        // 解决 fitSystemWindow、adjustResize、FLAG_TRANSLUCENT_STATUS 一起使用的bug
        keyboardUtil = new KeyboardUtil(this, findViewById(android.R.id.content));
        //enable it
        keyboardUtil.enable();
    }

    /**
     * 默认标题栏是否可见
     *
     * @return
     */
    protected boolean defaultTitleBarVisible() {
        return true;
    }

    /**
     * ToolBar自定义
     *
     * @param toolbar
     */
    protected void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
    }

    /**
     * 初始化一些View
     */
    protected void afterSetContentView() {

    }

    LogicHelper logicHelper = new LogicHelper();
    TaskHelper taskHelper = new TaskHelper();

    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     *
     * @param logic
     * @param <T>
     * @return
     */
    protected <T extends EventLogic> T registLogic(EventLogic logic) {
        return logicHelper.registLogic(logic);
    }

    /**
     * 注册Task, Activity销毁时是自动取消解绑
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T extends Task> T registTask(Task task) {
        return taskHelper.registTask(task);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        logicHelper.unregistAll();
        taskHelper.unregistAll();
        keyboardUtil.disable();
    }

    /**
     * EventBus订阅者事件通知的函数, UI线程
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Message msg) {
        if (!isDestroyed && !isFinishing()) {
            onResponse(msg);
        }
    }

    public abstract void onResponse(Message msg);
}
