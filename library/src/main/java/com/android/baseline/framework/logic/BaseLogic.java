package com.android.baseline.framework.logic;

import com.android.baseline.framework.logic.net.RetrofitManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * '数据'模块统一出口, Retrofit基本封装
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/3 11:07]
 */
public abstract class BaseLogic extends EventLogic {
    protected Retrofit retrofit;

    /**
     * 构造函数
     *
     * @param subscriber 最终订阅者
     */
    public BaseLogic(Object subscriber) {
        super(subscriber);
        retrofit = RetrofitManager.getInstance().getRetrofit(getBaseUrl(), networkInterceptor());
    }

    /**
     * create api service
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 发送请求(一般情况是可以拿到结果的最终请求,如需要'map、flatMap、doOnNext'等在BaseLogic的子类做好处理)
     *
     * @param observable
     * @param what       请求标示
     */
    public void sendRequest(final Observable observable, final int what) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        onResult(what, o);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        // 无网络、解析报错、404\500
                        // 无网络、解析报错、404\500
                        InfoResult<Throwable> infoResult = new InfoResult(InfoResult.INNER_ERROR_CODE);
                        infoResult.setData(throwable);
                        onResult(what, infoResult);
                    }
                });
    }

    /**
     * API根地址
     *
     * @return
     */
    protected abstract String getBaseUrl();

    /**
     *
     * @return
     */
    protected Interceptor networkInterceptor() {
        return null;
    }
}
