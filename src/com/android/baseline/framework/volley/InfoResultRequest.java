package com.android.baseline.framework.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.baseline.framework.logic.ILogic;
import com.android.baseline.framework.logic.InfoResult;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
/**
 * 重写Request实现InfoResult结果类型的网络请求, 做了以下扩展
 * [
 *   1、提供设置请求头header
 *   2、提供post方式设置消息体body
 * ]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-18]
 */
public class InfoResultRequest extends Request<InfoResult> implements Listener<InfoResult>
{
    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";
    /** request identifier*/
    private int requestId;
    /** request headers*/
    private Map<String, String> headers;
    /** 回调业务层做解析Bean封装*/
    private ResponseParserListener parserListener;
    /** 分发解析好的数据到业务层*/
    private ILogic logic;
    /** request body*/
    private String body;
    
    public InfoResultRequest(final int requestId, String url, ResponseParserListener parseListener, final ILogic logic)
    {
        this(requestId, url, Method.GET, null, null, parseListener, logic);
    }
    
    public InfoResultRequest(final int requestId, String url, int method, String body, ResponseParserListener parseListener, final ILogic logic)
    {
        this(requestId, url, method, body, null, parseListener, logic);
    }
    
    public InfoResultRequest(final int requestId, String url, Map<String, String> headers, ResponseParserListener parseListener, final ILogic logic)
    {
        this(requestId, url, Method.GET, null, headers, parseListener, logic);
    }
    
    public InfoResultRequest(final int requestId, String url, int method, String body, Map<String, String> headers, ResponseParserListener parseListener, final ILogic logic)
    {
        super(method, url, new ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                logic.onResult(requestId, error);
            }
        });
        this.body = body;
        this.headers = headers;
        this.parserListener = parseListener;
        this.requestId = requestId;
        this.logic = logic;
    }
    
    @Override
    public byte[] getBody() throws AuthFailureError
    {
        try {
            return body == null ? null : body.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    body, PROTOCOL_CHARSET);
            return null;
        }
    }
    
    @Override
    public void onResponse(InfoResult response)
    {
        logic.onResult(requestId, response);
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
            String str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            InfoResult infoResult = parserListener.doParse(str);
            if (infoResult == null) // 解析失败
            {
                return Response.error(new VolleyError("parse response error >>> " + str));
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

    /**
     * 通知调用者根据各自业务解析响应字符串到InfoResult
     * @author hiphonezhu@gmail.com
     * @version [Android-BaseLine, 2014-9-18]
     */
    public interface ResponseParserListener
    {
        InfoResult doParse(final String response) throws Exception;
    }
}
