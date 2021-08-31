package library.common.framework.task;

import android.annotation.SuppressLint;
import android.os.Message;

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
 * @version [AndroidLibrary, 2018-3-6]
 */
public class TaskExecutor {
    private static TaskExecutor sInstance;

    private TaskExecutor() {
    }

    public synchronized static TaskExecutor getInstance() {
        if (sInstance == null) {
            sInstance = new TaskExecutor();
        }
        return sInstance;
    }

    /**
     * 执行任务
     *
     * @param task
     */
    @SuppressLint("CheckResult")
    public void execute(final Task task) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Message> e) throws Exception {
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
                        task.callback(message);
                    }
                });

    }
}
