package com.android.baseline.framework.logic;

import com.android.baseline.framework.logic.net.RetrofitManager;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * '数据'模块统一出口, Retrofit基本封装
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/3 11:07]
 */
public abstract class BaseLogic extends EventLogic {
    protected Retrofit retrofit;

    /**
     * 构造函数
     * @param subscriber 最终订阅者
     */
    public BaseLogic(Object subscriber)
    {
        super(subscriber);
        retrofit = RetrofitManager.getInstance().getRetrofit(getBaseUrl());
    }

    /**
     * create api service
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 发送请求(一般情况是可以拿到结果的最终请求,如需要'map、flatMap、doOnNext'等在BaseLogic的子类做好处理)
     * @param observable
     * @param what 请求标示
     */
    public void sendRequest(final Observable observable, final int what)
    {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // 无网络、解析报错、404\500
                        onResult(what, e);
                    }

                    @Override
                    public void onNext(Object o) {
                        onResult(what, o);
                    }
                });
    }

    /**
     * API根地址
     * @return
     */
    protected String getBaseUrl()
    {
        return "http://apis.baidu.com/";
    }
}
