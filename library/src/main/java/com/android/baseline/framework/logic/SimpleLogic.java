package com.android.baseline.framework.logic;

import com.android.baseline.framework.logic.parser.JsonParser;
import com.android.baseline.framework.volley.InfoResultMultiPartRequest;
import com.android.baseline.framework.volley.InfoResultRequest;
import com.android.baseline.util.APKUtil;
import com.android.volley.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * BaseLogic简易使用封装
 * 提供GET、POST方式；文件、参数上传
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016-06-20]
 */
public class SimpleLogic extends BaseLogic
{
    /**
     * Constructor with a subscriber
     * @param subscriber
     */
    public SimpleLogic(Object subscriber)
    {
        this(subscriber, currentVolleyTag);
    }

    /**
     * Constructor with a subscriber and custom volleyTag
     * @param subscriber
     * @param volleyTag
     */
    public SimpleLogic(Object subscriber, String volleyTag)
    {
        this(subscriber, new EventBus(), volleyTag);
    }

    /**
     * Constructor with custom EventBus and volleyTag
     * @param eventBus
     * @param volleyTag
     */
    public SimpleLogic(Object subscriber, EventBus eventBus, String volleyTag)
    {
        super(subscriber, eventBus, volleyTag);
    }

    /**
     * GET方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param parser 对应返回结果的解析器
     */
    protected void sendGetRequest(int requestId, String url, JsonParser parser)
    {
        this.sendGetRequest(requestId, url, parser, null);
    }

    /**
     * GET方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param parser 对应返回结果的解析器
     * @param headers 请求头信息
     */
    protected void sendGetRequest(int requestId, String url, JsonParser parser, Map<String, String> headers)
    {
        this.sendGetRequest(requestId, url, null, parser, headers);
    }

    /**
     * GET方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param params 参数
     * @param parser 对应返回结果的解析器
     */
    protected void sendGetRequest(int requestId, String url, Map<String, Object> params, JsonParser parser)
    {
        this.sendGetRequest(requestId, url, params, parser, null);
    }

    /**
     * GET方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param params 参数
     * @param parser 对应返回结果的解析器
     * @param headers 请求头信息
     */
    protected void sendGetRequest(int requestId, String url, Map<String, Object> params, JsonParser parser, Map<String, String> headers)
    {
        this.sendGetRequest(requestId, url, params, parser, headers, currentVolleyTag);
    }

    /**
     * GET方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param params 参数
     * @param parser 对应返回结果的解析器
     * @param headers 请求头信息
     * @param volleyTag 使用的volley队列
     */
    protected void sendGetRequest(int requestId, String url, Map<String, Object> params, JsonParser parser, Map<String, String> headers, String volleyTag)
    {
        if (params != null)
        {
            String encodeParams = APKUtil.encodeParameters(params, GLOBAL_ENCODING);
            url += "?" + encodeParams;
        }
        InfoResultRequest request = new InfoResultRequest(requestId, url, parser, this);
        request.addHeaders(headers);
        sendRequest(request, requestId, volleyTag);
    }

    /**
     * POST方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param params 参数
     * @param parser 对应返回结果的解析器
     */
    protected void sendPostRequest(int requestId, String url, Map<String, Object> params, JsonParser parser)
    {
        this.sendPostRequest(requestId, url, params, parser, null);
    }

    /**
     * POST方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param params 参数
     * @param parser 对应返回结果的解析器
     * @param headers 请求头信息
     */
    protected void sendPostRequest(int requestId, String url, Map<String, Object> params, JsonParser parser, Map<String, String> headers)
    {
        this.sendPostRequest(requestId, url, params, parser, headers, currentVolleyTag);
    }

    /**
     * POST方式请求
     * @param requestId 请求标识
     * @param url 地址
     * @param params 参数
     * @param parser 对应返回结果的解析器
     * @param headers 请求头信息
     * @param volleyTag 使用的volley队列
     */
    protected void sendPostRequest(int requestId, String url, Map<String, Object> params, JsonParser parser, Map<String, String> headers, String volleyTag)
    {
        InfoResultRequest request = new InfoResultRequest(requestId, url, params, parser, this);
        request.addHeaders(headers);
        sendRequest(request, requestId, volleyTag);
    }

    /**
     * 文件上传
     * @param requestId
     * @param url
     * @param params 请求参数
     * @param files 文件名-文件路径
     * @param parser
     */
    protected void sendMultiPartRequest(int requestId, String url, Map<String, Object> params, Map<String, String> files, JsonParser parser)
    {
        this.sendMultiPartRequest(requestId, url, params, files, parser, null);
    }

    /**
     * 文件上传
     * @param requestId
     * @param url
     * @param params 请求参数
     * @param files 文件名-文件路径
     * @param parser
     */
    protected void sendMultiPartRequest(int requestId, String url, Map<String, Object> params, Map<String, String> files, JsonParser parser, Map<String, String> headers)
    {
        this.sendMultiPartRequest(requestId, url, params, files, parser, null, currentVolleyTag);
    }

    /**
     * 文件上传
     * @param requestId
     * @param url
     * @param params 请求参数
     * @param files 文件名-文件路径
     * @param parser
     */
    protected void sendMultiPartRequest(int requestId, String url, Map<String, Object> params, Map<String, String> files,
                                        JsonParser parser, Map<String, String> headers, String volleyTag)
    {
        InfoResultMultiPartRequest request = new InfoResultMultiPartRequest(requestId, url, Request.Method.POST, parser, this);
        // deal with files
        Set<String> fileNameSet = files.keySet();
        Iterator<String> fileNameIte = fileNameSet.iterator();
        while(fileNameIte.hasNext())
        {
            String fileName = fileNameIte.next();
            request.addFile(fileName, files.get(fileName));
        }
        // deal with params
        if (params != null)
        {
            Set<String> keySet = params.keySet();
            Iterator<String> paramsIte = keySet.iterator();
            while(paramsIte.hasNext())
            {
                String key = paramsIte.next();
                request.addMultipartParam(key, params.get(key).toString());
            }
        }
        request.addHeaders(headers);
        sendRequest(request, requestId, volleyTag);
    }
}
