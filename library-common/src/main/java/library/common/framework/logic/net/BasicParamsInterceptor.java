package library.common.framework.logic.net;


import android.text.TextUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 拦截器
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class BasicParamsInterceptor implements Interceptor {
    
    /**
     * @return 返回 GET 参数
     */
    public Map<String, String> getQueryParams() {
        return Collections.emptyMap();
    }
    
    /**
     * @return 返回 POST 参数
     */
    public Map<String, String> getParams() {
        return Collections.emptyMap();
    }
    
    /**
     * @return 返回头信息
     */
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }
    
    /**
     * @return 返回头信息
     */
    public List<String> getHeaderLines() {
        return Collections.emptyList();
    }
    
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Map<String, String> queryParamsMap = getQueryParams();
        Map<String, String> paramsMap = getParams();
        Map<String, String> headerParamsMap = getHeaders();
        List<String> headerLinesList = getHeaderLines();
        
        
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        
        // process header params inject
        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (headerParamsMap.size() > 0) {
            Iterator iterator = headerParamsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        
        if (headerLinesList.size() > 0) {
            for (String line : headerLinesList) {
                headerBuilder.add(line);
            }
        }
        requestBuilder.headers(headerBuilder.build());
        // process header params end
        
        
        // process queryParams inject whatever it's GET or POST
        if (queryParamsMap.size() > 0) {
            request = injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, queryParamsMap);
        }
        
        // process post body inject
        if (paramsMap.size() > 0) {
            if (canInjectIntoBody(request)) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    formBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
                }
                
                RequestBody formBody = formBodyBuilder.build();
                String postBodyString = bodyToString(request.body());
                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
            }
        }
        
        request = requestBuilder.build();
        return chain.proceed(request);
    }
    
    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
            return false;
        }
        return true;
    }
    
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }
        
        return null;
    }
    
    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
