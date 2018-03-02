package com.android.baseline.framework.task;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    public void unregister(Object subscriber) {
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
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Message> e) throws Exception {
                // 使用Task反向注册EventBus,为了外层能够统一解除所有Task的订阅者
                task.register(eventBus);

                try {
                    e.onNext(task.execute());
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(@NonNull Message message) throws Exception {
                        eventBus.post(message);
                    }
                });

    }
}
