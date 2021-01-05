package library.common.framework.ui.activity.presenter;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import library.common.util.Callback;

/**
 * Presenter base class for Fragment
 * Presenter层的实现基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class FragmentPresenter<T extends IDelegate> extends Fragment implements LogicCallback {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewDelegate.create(this, inflater, container, savedInstanceState);
        return viewDelegate.getContentView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isDestroyed = false;
        viewDelegate.initWidget(getArguments());
        viewDelegate.initChildControllers();
        onCreate();
    }

    protected void onCreate() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onHide();
        } else {
            onShow();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (isVisibleToUser) {
                onShow();
            } else {
                onHide();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && getUserVisibleHint()) {
            onShow();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden() && getUserVisibleHint()) {
            onHide();
        }
    }

    /**
     * Fragment 可见
     * <p>
     * 如何得到Fragment页面的Show/Hide页面事件？
     * 　　由于fragment使用场景比较多样，单单依靠OnResume/OnPause两个回调表示fragment Show/Hide是不准确的,比如：
     * 场景一：
     * 　　首页一个Activity承载多个Fragment Tab的情况，此时tab间切换并不会触发Fragment的OnResume/OnPause．触发的回调函数是onHiddenChanged(boolean hidden)
     * 场景二：
     * 　　一个ViewPager承载多个页面的Fragment时
     * 　　　　a.当第一个Fragment1显示时，虽然第二个Fragment2此时尚未显示，但是Fragment2的OnResume却以及执行，处于resumed的状态．
     * 　　　　b.ViewPager页面切换OnResume/OnPause/onHiddenChanged均未触发，触发的回调是setUserVisibleHint
     * 　　此时判断Fragment　Show/Hide应该用setUserVisibleHint，而非OnResume/OnPause
     */
    public void onShow() {
        viewDelegate.onShow();
    }

    /**
     * Fragment 不可见
     * <p>
     * 如何得到Fragment页面的Show/Hide页面事件？
     * 　　由于fragment使用场景比较多样，单单依靠OnResume/OnPause两个回调表示fragment Show/Hide是不准确的,比如：
     * 场景一：
     * 　　首页一个Activity承载多个Fragment Tab的情况，此时tab间切换并不会触发Fragment的OnResume/OnPause．触发的回调函数是onHiddenChanged(boolean hidden)
     * 场景二：
     * 　　一个ViewPager承载多个页面的Fragment时
     * 　　　　a.当第一个Fragment1显示时，虽然第二个Fragment2此时尚未显示，但是Fragment2的OnResume却以及执行，处于resumed的状态．
     * 　　　　b.ViewPager页面切换OnResume/OnPause/onHiddenChanged均未触发，触发的回调是setUserVisibleHint
     * 　　此时判断Fragment　Show/Hide应该用setUserVisibleHint，而非OnResume/OnPause
     */
    public void onHide() {
        viewDelegate.onHide();
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

    public <R> void observe(final LiveData<R> liveData, final Observer<R> observer) {
        liveData.observe(getViewLifecycleOwner(), new Observer<R>() {
            @Override
            public void onChanged(R r) {
                observer.onChanged((R) beforeOnChanged(r));
            }
        });
    }

    protected abstract Object beforeOnChanged(Object res);
}
