package com.android.baseline.framework.ui.activity.base;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;

import com.android.baseline.AppDroid;
import com.android.baseline.framework.logic.EventLogic;
import com.android.baseline.framework.task.Task;
import com.android.baseline.framework.ui.activity.base.helper.LogicHelper;
import com.android.baseline.framework.ui.activity.base.helper.TaskHelper;
import com.android.baseline.framework.ui.activity.presenter.FragmentPresenter;
import com.android.baseline.framework.ui.activity.view.IDelegate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 基类Fragment, 提供业务逻辑的处理和深层的UI处理
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-4-1]
 */
public abstract class BaseFragment<I extends IDelegate> extends FragmentPresenter {
    LogicHelper logicHelper = new LogicHelper();
    TaskHelper taskHelper = new TaskHelper();
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDroid.getInstance().uiStateHelper.addFragment(this);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        logicHelper.unregistAll();
        taskHelper.unregistAll();
        AppDroid.getInstance().uiStateHelper.removeFragment(this);
    }
    
    /**
     * 注册BaseLogic, Fragment销毁时是自动取消解绑
     *
     * @param logic
     * @param <T>
     * @return
     */
    protected <T extends EventLogic> T findLogic(EventLogic logic) {
        return logicHelper.findLogic(logic);
    }
    
    /**
     * 注册Task, Fragment销毁时是自动取消解绑
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T extends Task> T findTask(Task task) {
        return taskHelper.findTask(task);
    }
    
    
    /**
     * EventBus订阅者事件通知的函数, UI线程
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Message msg) {
        if (isAdded() && !isDetached() && !isRemoving()) {
            onResponse(msg);
        }
    }
    
    protected abstract void onResponse(Message msg);
}
