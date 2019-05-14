package library.common.framework.ui.activity.presenter;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import library.common.framework.logic.EventLogic;
import library.common.framework.logic.LogicCallback;
import library.common.framework.task.Task;
import library.common.framework.ui.activity.base.helper.LogicHelper;
import library.common.framework.ui.activity.base.helper.TaskHelper;
import library.common.framework.ui.activity.view.IDelegate;
import library.common.util.Callback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Presenter base class for DialogFragment
 * Presenter层的实现基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class DialogPresenter<T extends IDelegate> extends DialogFragment implements LogicCallback {
    public T viewDelegate;
    
    Callback callback;
    
    public <T> void setCallback(Callback<T> callback) {
        this.callback = callback;
    }
    
    public <T> void doCall(T data) {
        if (callback != null) {
            callback.call(data);
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onShow();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        viewDelegate.onHide();
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDelegate.create(this, inflater, container, savedInstanceState);
        return viewDelegate.getContentView();
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isDestroyed = false;
        viewDelegate.initWidget(getArguments());
        onCreate();
    }
    
    protected void onCreate() {
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (viewDelegate.getOptionsMenuId() != 0) {
            inflater.inflate(viewDelegate.getOptionsMenuId(), menu);
        }
    }
    
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
        viewDelegate = null;
        
        isDestroyed = true;
        logicHelper.unregisterAll();
        taskHelper.unregisterAll();
    }
    
    protected abstract Class<T> getDelegateClass();
    
    LogicHelper logicHelper = new LogicHelper();
    TaskHelper taskHelper = new TaskHelper();
    boolean isDestroyed;
    
    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     *
     * @param logic
     * @param <T>
     * @return
     */
    protected <T extends EventLogic> T findLogic(EventLogic logic) {
        return logicHelper.findLogic(logic);
    }
    
    /**
     * 注册Task, Activity销毁时是自动取消解绑
     *
     * @param task
     * @param <T>
     * @return
     */
    protected <T extends Task> T findTask(Task task) {
        return taskHelper.findTask(task);
    }
    
    /**
     * EventBus订阅者事件通知的函数, UI线程
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Message msg) {
        if (!isDestroyed && !isDetached()) {
            onResponse(msg);
        }
    }
    
    /**
     * 业务层回调
     *
     * @param msg
     */
    @Override
    public void call(Message msg) {
        if (!isDestroyed && !isDetached()) {
            onResponse(msg);
        }
    }
    
    protected void onResponse(Message msg) {
    
    }
}
