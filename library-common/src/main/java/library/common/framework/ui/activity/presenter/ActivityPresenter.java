/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package library.common.framework.ui.activity.presenter;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

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
        initToolbar();
        isDestroyed = false;
        viewDelegate.initWidget();
        setScrimColor(0x00000000);
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

    protected void initToolbar() {
        Toolbar toolbar = viewDelegate.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewDelegate.getOptionsMenuId() != 0) {
            getMenuInflater().inflate(viewDelegate.getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
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
}
