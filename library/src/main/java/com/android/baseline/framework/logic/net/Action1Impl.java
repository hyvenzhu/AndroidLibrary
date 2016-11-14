package com.android.baseline.framework.logic.net;

import com.android.baseline.framework.logic.InfoResult;

import rx.functions.Action1;

/**
 * Action1 实现类, 可以在这里做一些统一处理
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/11/14 17:55]
 */

public abstract class Action1Impl<T> implements Action1<InfoResult<T>> {
    @Override
    public void call(InfoResult<T> infoResult) {
        // 这里做一些全局处理

        // keep call
        nextCall(infoResult);
    }

    public abstract void nextCall(InfoResult<T> infoResult);
}
