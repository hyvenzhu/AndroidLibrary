package library.common.framework.ui.activity.presenter;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import library.common.framework.logic.EventLogic;
import library.common.framework.logic.LogicCallback;
import library.common.framework.task.Task;
import library.common.framework.ui.activity.base.helper.LogicHelper;
import library.common.framework.ui.activity.base.helper.TaskHelper;
import library.common.framework.ui.activity.view.IDelegate;
import library.common.framework.ui.swipeback.app.SwipeBackActivity;

/**
 * Presenter base class for Activity
 * Presenter层的实现基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class ActivityPresenter<T extends IDelegate> extends SwipeBackActivity implements LogicCallback {
    @NonNull
    public T viewDelegate;

    public ActivityPresenter() {
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("create IDelegate error");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate.create(this, getLayoutInflater(), null, savedInstanceState);
        setContentView(viewDelegate.getContentView());
        isDestroyed = false;
        viewDelegate.initWidget(getIntent());
        viewDelegate.initChildControllers();
        onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onShow();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewDelegate.onHide();
    }

    protected void onCreate() {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    @Override
    protected void onDestroy() {
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
    public boolean isDestroyed;

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
        if (!isDestroyed && !isFinishing()) {
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
        if (!isDestroyed && !isFinishing()) {
            onResponse(msg);
        }
    }

    protected void onResponse(Message msg) {

    }

    public <R> void observe(final LiveData<R> liveData, final Observer<R> observer) {
        liveData.observe(this, new Observer<R>() {
            @Override
            public void onChanged(R r) {
                observer.onChanged((R) beforeOnChanged(r));
            }
        });
    }

    protected abstract Object beforeOnChanged(Object res);
}
