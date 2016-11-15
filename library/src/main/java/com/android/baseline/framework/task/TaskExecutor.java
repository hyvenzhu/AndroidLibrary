package com.android.baseline.framework.task;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 异步任务执行器封装,使用RxAndroid实现
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/8/17 13:43]
 */
public class TaskExecutor {
    private static TaskExecutor sInstance;
    EventBus eventBus;

    private TaskExecutor() {
        eventBus = EventBus.getDefault();
    }

    public synchronized static TaskExecutor getInstance() {
        if (sInstance == null) {
            sInstance = new TaskExecutor();
        }
        return sInstance;
    }

    /**
     * 主动解除EventBus的订阅
     *
     * @param subscriber
     */
    public void unregist(Object subscriber) {
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    /**
     * 执行任务
     *
     * @param task
     */
    public void execute(final Task task) {
        Observable.create(new Observable.OnSubscribe<Message>() {
            @Override
            public void call(Subscriber<? super Message> subscriber) {
                subscriber.onNext(task.execute());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        // 使用Task反向注册EventBus,为了外层能够统一解除所有Task的订阅者
                        task.register(eventBus);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Message msg) {
                        eventBus.post(msg);
                    }
                });
    }
}
