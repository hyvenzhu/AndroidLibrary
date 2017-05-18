package common;

import com.android.baseline.framework.logic.BaseLogic;

import okhttp3.Interceptor;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/5/18 17:55]
 */

public class MyLogic extends BaseLogic {
    /**
     * 构造函数
     *
     * @param subscriber 最终订阅者
     */
    public MyLogic(Object subscriber) {
        super(subscriber);
    }

    @Override
    protected String getBaseUrl() {
        return "http://apis.baidu.com/";
    }

    @Override
    protected Interceptor networkInterceptor() {
        return null;
    }
}
