package com.android.baseline.framework.ui.activity.base;

import android.app.Fragment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.baseline.framework.logic.EventLogic;
import com.android.baseline.framework.task.Task;
import com.android.baseline.framework.ui.activity.annotations.ViewUtils;
import com.android.baseline.framework.ui.activity.base.helper.TaskHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 基类Fragment, 提供业务逻辑的处理和深层的UI处理
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-4-1]
 */
public abstract class BaseFragment extends Fragment {
    private View mView;

    /**
     * 从资源加载View
     *
     * @param inflater
     * @param container
     * @param resourceId
     * @param fragment
     * @return
     */
    protected View inflate(LayoutInflater inflater, ViewGroup container, int resourceId, Fragment fragment) {
        View view = inflater.inflate(resourceId, container, false);
        // 屏蔽Fragment布局的点击事件, 否则事件可能会被“栈”下面的Fragment捕获
        interceptTouchEvent(view, true);
        ViewUtils.inject(fragment, view);
        mView = view;
        afterSetContentView(view);
        return view;
    }

    com.android.baseline.framework.ui.activity.base.helper.LogicHelper logicHelper = new com.android.baseline.framework.ui.activity.base.helper.LogicHelper();
    TaskHelper taskHelper = new TaskHelper();

    /**
     * 注册BaseLogic, Fragment销毁时是自动取消解绑
     *
     * @param logic
     * @param <T>
     * @return
     */
    protected <T extends EventLogic> T registLogic(EventLogic logic) {
        return logicHelper.registLogic(logic);
    }

    /**
     * 注册Task, Fragment销毁时是自动取消解绑
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T extends Task> T registTask(Task task) {
        return taskHelper.registTask(task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logicHelper.unregistAll();
        taskHelper.unregistAll();
    }

    /**
     * setContentView之后调用, 进行view的初始化等操作
     */
    protected void afterSetContentView(View view) {

    }

    /**
     * Fragment布局是否拦截事件
     *
     * @param interceptEvent true拦截|false不拦截
     */
    protected void interceptTouchEvent(boolean interceptEvent) {
        interceptTouchEvent(mView, interceptEvent);
    }

    /**
     * Fragment布局是否拦截事件
     *
     * @param view
     * @param interceptEvent true拦截|false不拦截
     */
    private void interceptTouchEvent(View view, boolean interceptEvent) {
        if (interceptEvent) {
            if (view != null) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
        } else {
            if (view != null) {
                view.setOnTouchListener(null);
            }
        }
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
