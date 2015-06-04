package com.android.baseline.framework.volley;

import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.logic.ILogic;
import com.android.baseline.framework.logic.InfoResult;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
/**
 * 重写Request实现InfoResult结果类型的网络请求, 做了以下扩展
 * [
 *   1、提供设置请求头header
 *   2、支持返回InputStream数据
 *   3、支持同步请求(其他所有请求必须等待某个请求结束，否则在缓冲队列排队)。
 *      并支持同步请求结束后其他请求触发的回调，可以其他修改请求的数据等
 * ]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-18]
 */
public class InfoResultRequest extends Request<InfoResult> implements Listener<InfoResult>
{
    /** request identifier*/
    private int requestId;
    /** request headers*/
    private Map<String, String> headers;
    /** 回调业务层做解析Bean封装*/
    private ResponseParserListener parserListener;
    /** 分发解析好的数据到业务层*/
    private ILogic logic;
    /** request params*/
    private Map<String, Object> params;
    /** 是否返回流*/
    private boolean isNeedStream;
    /** 知否是同步阻塞型请求*/
    private boolean isSyncRequest;

    public InfoResultRequest(int requestId, String url, ResponseParserListener parseListener, ILogic logic)
    {
        this(requestId, url, Method.GET, null, parseListener, logic);
    }

    public InfoResultRequest(int requestId, String url, Map<String, Object> params, ResponseParserListener parseListener, ILogic logic)
    {
        this(requestId, url, Method.POST, params, parseListener, logic);
    }

    public InfoResultRequest(final int requestId, String url, int method, Map<String, Object> params, final ResponseParserListener parseListener, final ILogic logic)
    {
        super(method, url, new ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Logger.w("InfoResultMultiPartRequest", error);
                try
                {
                    // 错误
                    InfoResult infoResult = parseListener.doParse("{\"success\": \"false\",\"code\": -100,\"desc\": \"\",\"data\": {}}");
                    infoResult.setExtraObj(error);
                    logic.onResult(requestId, infoResult);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        this.params = params;
        this.parserListener = parseListener;
        this.requestId = requestId;
        this.logic = logic;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);
    }
    
    @Override
    protected Map<String, Object> getParams() throws AuthFailureError
    {
        return params;
    }
    
    /**
     * 修改参数值
     * @param key 键
     * @param value 值
     */
    public void setParamValue(String key, String value)
    {
        if (params != null)
        {
            params.put(key, value);
        }
    }
    
    /**
     * 更新请求数据
     */
    public void updateRequest()
    {
        
    }
    
    @Override
    public void onResponse(InfoResult response)
    {
        logic.onResult(requestId, response);
    }
    
    /**
     * 设置头信息
     * @param headers
     */
    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }
    
    /**
     * 添加头信息
     * @param headers
     */
    public void addHeaders(Map<String, String> headers)
    {
        if (this.headers == null || headers == null)
        {
            setHeaders(headers);
        }
        else
        {
            this.headers.putAll(headers);
        }
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        if (headers != null)
        {
            return headers;
        }
        else
        {
            return super.getHeaders();
        }
    }
    
    @Override
    protected Response<InfoResult> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            InfoResult infoResult = null;
            if (isNeedStream())
            {
                infoResult = parserListener.doParse(response.is);
            }
            else
            {
                String str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                infoResult = parserListener.doParse(str);
            }
            if (infoResult == null) // 解析失败
            {
                return Response.error(new VolleyError("parse response error"));
            }
            else // 解析成功
            {
                return Response.success(infoResult, HttpHeaderParser.parseCacheHeaders(response));
            }
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new VolleyError("UnsupportedEncodingException"));
        }
        catch (Exception e)
        {
            return Response.error(new VolleyError("Exception is >>> " + e.getMessage()));
        }  
    }

    @Override
    protected void deliverResponse(InfoResult response)
    {
        this.onResponse(response);
    }
    
    public void setNeedStream(boolean isNeedStream) {
        this.isNeedStream = isNeedStream;
        if (isNeedStream)
        {
            setShouldCache(false);
        }
    }

    @Override
    public boolean isNeedStream() {
        return isNeedStream;
    }
    
    /**
     * 是否是同步的请求, 如果是则其他所有请求都必须等待该请求结束才能执行
     * @return
     */
    public boolean isSyncRequest()
    {
        return isSyncRequest;
    }
    
    public void setSyncRequest(boolean isSyncRequest)
    {
        this.isSyncRequest = isSyncRequest;
    }

    public int getRequestId()
    {
        return requestId;
    }

    /**
     * 通知调用者根据各自业务解析响应字符串到InfoResult
     * @author hiphonezhu@gmail.com
     * @version [Android-BaseLine, 2014-9-18]
     */
    public interface ResponseParserListener
    {
        InfoResult doParse(final String response) throws Exception;
        InfoResult doParse(final InputStream response) throws Exception;
    }
}
