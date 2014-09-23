package com.android.baseline.framework.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Message;

import com.android.baseline.AppDroid;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import de.greenrobot.event.EventBus;

/**
 * 1、提供订阅者注册、解绑的实现
 * 2、提供统一的Volley请求队列
 * @author hiphonezhu@gmail.com
 * @version [BaseLine_Android_V5, 2014-9-15]
 */
public class BaseLogic implements ILogic
{
    // 存储所有的订阅者
    private List<Object> subscribers = new ArrayList<Object>();
    // Volley请求队列
    protected static RequestQueue requestQueue = Volley.newRequestQueue(AppDroid
            .getInstance().getApplicationContext());

    @Override
    public void register(Object subscriber)
    {
        if (!subscribers.contains(subscriber))
        {
            EventBus.getDefault().register(subscriber);
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(Object subscriber)
    {
        if (subscribers.contains(subscriber))
        {
            EventBus.getDefault().unregister(subscriber);
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void unregisterAll()
    {
        for (Object subscriber : subscribers)
        {
            EventBus.getDefault().unregister(subscriber);
        }
        subscribers.clear();
    }
    
    /**
     * 取消某一个网络请求
     * @param tag 某次请求的唯一标识
     */
    public void cancelAll(Object tag)
    {
        requestQueue.cancelAll(tag);
    }

    /**
     * 负责封装结果内容, post给订阅者
     * 
     * @param action 任务标识
     * @param response 响应结果 
     *                 instanceof VolleyError表示网络请求出错
     *                 instanceof InfoResult表示网络请求成功
     */
    @Override
    public void onResult(int action, Object response)
    {
        Message msg = new Message();
        msg.what = action;
        msg.obj = response;
        EventBus.getDefault().post(msg);
    }
    
    /**
     * 解析服务器结果的状态信息(业务成功与失败, 对应错误码和描述信息等)
     * @param jsonObject
     * @return
     */
    protected InfoResult parseLogicSatus(JSONObject jsonObject)
    {
        return new InfoResult.Builder()
                   .success(jsonObject.optBoolean("success"))
                   .errorCode(jsonObject.optString("errorCode"))
                   .desc(jsonObject.optString("desc"))
                   .build();
    }
}
