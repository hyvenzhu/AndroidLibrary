package com.android.baseline.framework.logic;

import java.util.ArrayList;
import java.util.List;

import android.os.Message;

import com.android.baseline.AppDroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import de.greenrobot.event.EventBus;

/**
 * 1、统一的Volley请求队列以及Request的发送
 * 2、使用EventBus通讯订阅者与发送者
 * 3、提供订阅者注册、解绑和业务请求结果统一分发的实现
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class BaseLogic implements ILogic
{
    // 存储所有的订阅者
    private List<Object> subscribers = new ArrayList<Object>();
    // Volley请求队列
    private static RequestQueue requestQueue = Volley.newRequestQueue(AppDroid
            .getInstance().getApplicationContext());
    // Custom EventBus
    private EventBus mEventBus;
    
    /**
     * Constructor with a new EventBus
     */
    public BaseLogic()
    {
        this(new EventBus());
    }
    
    /**
     * Constructor with custom EventBus
     * @param eventBus
     */
    public BaseLogic(EventBus eventBus)
    {
        if (eventBus == null)
        {
            mEventBus = EventBus.getDefault();
        }
        else
        {
            mEventBus = eventBus;
        }
    }
    
    /**
     * Get eventbus by default or custom
     * @return
     */
    private EventBus getDefault()
    {
        return mEventBus;
    }
    
    @Override
    public void register(Object subscriber)
    {
        if (!subscribers.contains(subscriber))
        {
            getDefault().register(subscriber);
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(Object subscriber)
    {
        if (subscribers.contains(subscriber))
        {
            getDefault().unregister(subscriber);
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void unregisterAll()
    {
        for (Object subscriber : subscribers)
        {
            getDefault().unregister(subscriber);
        }
        subscribers.clear();
    }
    
    /**
     * 取消某一个网络请求
     * @param tag 某次请求的唯一标识
     */
    protected void cancelAll(Object tag)
    {
        requestQueue.cancelAll(tag);
    }
    
    /**
     * 发送网络请求
     * @param <T>
     * @param request
     */
    protected <T> void sendRequest(Request<T> request)
    {
        sendRequest(request, null);
    }
    
    /**
     * 发送网络请求, 并给这个请求设置TAG
     * @param <T>
     * @param request
     * @param tag
     */
    protected <T> void sendRequest(Request<T> request, Object tag)
    {
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 负责封装结果内容, post给订阅者
     * [
     *   一、针对当前框架做了修改
     *       同一种类型的事件不会发送给多个订阅者(EventBus默认情况会发送个多个订阅者)
     *     
     *   二、如果需要同一类型的事件发送给多个订阅者
     *       使用BaseLogic(EventBus eventBus)构造函数, 为每个订阅者提供同一个EventBus对象(例如EventBus.getDefault()), 这样会出现以下情况
     *       1、msg的what相同的情况会被多个订阅者接受并处理
     *       2、msg的what不相同的话会被多个订阅者接受, 但不会被处理(具体需要业务层控制)
     * ]
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
        getDefault().post(msg);
    }
}
