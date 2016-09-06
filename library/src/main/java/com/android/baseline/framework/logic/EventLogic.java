package com.android.baseline.framework.logic;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护Logic的订阅者,以及提供取消订阅等方法
 * @version [Android-BaseLine, 16/9/3 11:07]
 */
public class EventLogic
{
    // 存储所有的订阅者
    private List<Object> subscribers = new ArrayList<Object>();
    // Default EventBus
    protected EventBus mEventBus;
    
    /**
     * Constructor with a subscriber and custom volleyTag
     * @param subscriber
     */
    public EventLogic(Object subscriber)
    {
        this(subscriber, new EventBus());
    }

    /**
     * Constructor with custom EventBus and volleyTag
     * @param eventBus
     */
    public EventLogic(Object subscriber, EventBus eventBus)
    {
        if (eventBus == null)
        {
            mEventBus = EventBus.getDefault();
        }
        else
        {
            mEventBus = eventBus;
        }
        register(subscriber);
    }

    public void register(Object subscriber)
    {
        if (!subscribers.contains(subscriber))
        {
            mEventBus.register(subscriber);
            subscribers.add(subscriber);
        }
    }

    public void unregister(Object subscriber)
    {
        if (subscribers.contains(subscriber))
        {
            mEventBus.unregister(subscriber);
            subscribers.remove(subscriber);
        }
    }

    public void unregisterAll()
    {
        for (Object subscriber : subscribers)
        {
            mEventBus.unregister(subscriber);
        }
        subscribers.clear();
    }

    /**
     * 负责封装结果内容, post给订阅者
     * @param what 任务标识
     * @param obj 响应结果
     */
    public void onResult(int what, Object obj)
    {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        mEventBus.post(msg);
    }
}
