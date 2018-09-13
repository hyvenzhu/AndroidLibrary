package android.demo.base;

import library.common.framework.logic.net.BasicParamsInterceptor;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public class DemoBaseLogic extends library.common.framework.logic.BaseLogic {
    /**
     * 构造函数
     *
     * @param subscriber 最终订阅者
     */
    public DemoBaseLogic(Object subscriber) {
        super(subscriber);
    }
    
    @Override
    protected String getBaseUrl() {
        return "http://10.100.122.34:3000";
    }
    
    @Override
    protected Interceptor interceptor() {
        return interceptor;
    }
    
    public static BasicParamsInterceptor interceptor = new BasicParamsInterceptor() {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("accessToken", "xxx");
            return headers;
        }
    };
}
