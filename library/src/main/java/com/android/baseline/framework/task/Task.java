package com.android.baseline.framework.task;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;

/**
 * 任务父类, 子类继承并实现doInBackground()方法, 在其中执行耗时操作
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-5-21]
 */
public abstract class Task implements ITask {
    int taskId;
    private Object subscriber;
    EventBus eventBus;

    public Task(int taskId, Object subscriber) {
        this.taskId = taskId;
        this.subscriber = subscriber;
    }

    /**
     * 封装结果,将doInBackground()与taskId合并成Message
     *
     * @return
     */
    public Message execute() {
        Message msg = new Message();
        msg.what = taskId;
        msg.obj = doInBackground();
        return msg;
    }

    public void register(EventBus eventBus) {
        this.eventBus = eventBus;
        if (eventBus != null && !eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber);
        }
    }

    public void unregister() {
        if (eventBus != null && eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }
}
