package library.common.framework.logic.net;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载进度监听拦截器
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-7-24]
 */
public class ProgressResponseInterceptor implements Interceptor {
    
    static final Map<String, WeakReference<IProgress>> LISTENER_MAP = new HashMap<>();
    
    /**
     * 设置下载进度回调
     *
     * @param url 被监听的 url
     * @param listener
     */
    public static void addListener(String url, IProgress listener) {
        LISTENER_MAP.put(url, new WeakReference<>(listener));
    }
    
    /**
     * 移除下载进度回调
     * @param url 被监听的 url
     */
    public static void removeListener(String url) {
        LISTENER_MAP.remove(url);
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        ResponseBody body = response.body();
        Response newResponse = response.newBuilder().body(new ProgressResponseBody(url, body)).build();
        return newResponse;
    }
}
