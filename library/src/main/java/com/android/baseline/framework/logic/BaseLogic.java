package com.android.baseline.framework.logic;

import android.os.Message;
import android.text.TextUtils;

import com.android.baseline.AppDroid;
import com.android.baseline.framework.volley.InfoResultRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.greenrobot.event.EventBus;

/**
 * 1、统一的Volley请求队列以及Request的发送
 * 2、使用EventBus通讯订阅者与发送者
 * 3、提供订阅者注册、解绑和业务请求结果统一分发的实现
 * 4、支持设置不同的RequestQueue, 且可以动态切换Volley队列
 * [
 *   一、针对当前框架做了修改
 *       每一个BaseLogic对象默认情况下使用不同的EventBus{@link #BaseLogic(Object subscriber)}, 
 *       保证同一种类型的事件不会发送给多个订阅者(EventBus默认情况会发送个多个订阅者)
 *     
 *   二、如果需要同一类型的事件发送给多个订阅者
 *       使用{@link #BaseLogic(Object subscriber, EventBus eventBus, String volleyTag)}构造函数, 为每个订阅者提供同一个EventBus对象(例如EventBus.getDefault()),
 *       这样会出现以下情况
 *       1、msg的what相同的情况会被多个订阅者接受并处理
 *       2、msg的what不相同的话会被多个订阅者接受, 但不会被处理(具体需要业务层控制)
 * ]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class BaseLogic implements ILogic
{
    // 存储所有的订阅者
    private List<Object> subscribers = new ArrayList<Object>();
    // 默认Volley请求队列
    private final static RequestQueue DEFAULT_REQUEST_QUEUE = Volley.newRequestQueue(AppDroid
            .getInstance().getApplicationContext());
    // Default EventBus
    private EventBus mEventBus;
    
    // 请求的tags
    private Set<Object> requestTags = new HashSet<Object>();
    
    private static Object lock = new Object(); // 锁对象
    private static boolean isSyncRequesting; // 是否正在执行同步请求
    private static int syncRequestId = -1000; // 同步请求Id
    private static ConcurrentLinkedQueue<Request<?>> waitQueue = new ConcurrentLinkedQueue<Request<?>>(); // 缓存后续的请求

    // Volley队列池
    private static Map<String, RequestQueue> requestQueuePool = new HashMap<String, RequestQueue>();
    // 默认Volley tag
    public static final String DEFAULT_VOLLEY_TAG = "default";
    static
    {
        requestQueuePool.put(DEFAULT_VOLLEY_TAG, DEFAULT_REQUEST_QUEUE);
    }
    // 当前队列标识
    private static String currentVolleyTag = DEFAULT_VOLLEY_TAG;
    // 当前Volley队列
    private RequestQueue currentRequestQueue;

    /**
     * Constructor with a subscriber
     * @param subscriber
     */
    public BaseLogic(Object subscriber)
    {
        this(subscriber, new EventBus(), currentVolleyTag);
    }

    /**
     * Constructor with a subscriber and custom volleyTag
     * @param subscriber
     * @param volleyTag
     */
    public BaseLogic(Object subscriber, String volleyTag)
    {
        this(subscriber, new EventBus(), volleyTag);
    }

    /**
     * Constructor with custom EventBus and volleyTag
     * @param eventBus
     * @param volleyTag
     */
    public BaseLogic(Object subscriber, EventBus eventBus, String volleyTag)
    {
        if (TextUtils.isEmpty(volleyTag))
        {
            volleyTag = currentVolleyTag;
        }
        currentRequestQueue = requestQueuePool.get(volleyTag);

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

    /**
     * 添加请求队列
     * @param volleyTag
     * @param requestQueue
     */
    public static void injectVolleyQueue(String volleyTag, RequestQueue requestQueue)
    {
        if (TextUtils.isEmpty(volleyTag))
        {
            throw new RuntimeException("volleyTag can not be null!");
        }
        if (requestQueue == null)
        {
            throw new RuntimeException("requestQueue can not be null!");
        }

        requestQueuePool.put(volleyTag, requestQueue);
    }

    /**
     * 设置默认队列
     * @param volleyTag
     */
    public static void setDefaultVolleyQueue(String volleyTag)
    {
        if (TextUtils.isEmpty(volleyTag))
        {
            throw new RuntimeException("volleyTag can not be null!");
        }
        if (requestQueuePool.get(volleyTag) == null)
        {
            throw new RuntimeException("can not find RequestQueue with volleyTag: " + volleyTag);
        }

        currentVolleyTag = volleyTag;
    }

    @Override
    public void register(Object subscriber)
    {
        if (!subscribers.contains(subscriber))
        {
            mEventBus.register(subscriber);
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(Object subscriber)
    {
        if (subscribers.contains(subscriber))
        {
            mEventBus.unregister(subscriber);
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void unregisterAll()
    {
        for (Object subscriber : subscribers)
        {
            mEventBus.unregister(subscriber);
        }
        subscribers.clear();
    }
    
    /**
     * 取消某一个网络请求
     * @param tag 某次请求的唯一标识
     */
    @Override
    public void cancel(Object tag)
    {
        currentRequestQueue.cancelAll(tag);
    }
    
    /**
     * 取消所有网络请求
     */
    @Override
    public void cancelAll()
    {
        Iterator<Object> tagIterator = requestTags.iterator();
        while(tagIterator.hasNext())
        {
            cancel(tagIterator.next());
        }
    }
    
    /**
     * 发送网络请求
     * @param <T>
     * @param request
     */
    protected <T> void sendRequest(Request<T> request)
    {
        currentRequestQueue.add(request);
    }
    
    /**
     * 发送网络请求, 并给这个请求设置TAG
     * @param <T>
     * @param request
     * @param tag
     */
    protected <T> void sendRequest(Request<T> request, Object tag)
    {
        sendRequest(request, tag, currentVolleyTag);
    }

    /**
     * 发送网络请求, 并给这个请求设置TAG
     * @param <T>
     * @param request
     * @param tag
     * @param volleyTag 请求队列标识
     */
    protected <T> void sendRequest(Request<T> request, Object tag, String volleyTag)
    {
        if (tag != null)
        {
            request.setTag(tag);
        }

        // 校验队列是否存在
        if (TextUtils.isEmpty(volleyTag))
        {
            volleyTag = currentVolleyTag;
        }

        currentRequestQueue = requestQueuePool.get(volleyTag);
        if (currentRequestQueue == null)
        {
            throw new RuntimeException("can not find RequestQueue with volleyTag: " + volleyTag);
        }

        // 记录使用的请求队列
        request.setVolleyTag(volleyTag);

        synchronized (lock)
        {
            if (isSyncRequesting)
            {
                waitQueue.add(request);
            }
            else
            {
                if (request instanceof InfoResultRequest)
                {
                    InfoResultRequest infoResultRequest = (InfoResultRequest)request;
                    if (infoResultRequest.isSyncRequest())
                    {
                        syncRequestId = infoResultRequest.getRequestId();
                        isSyncRequesting = true;
                    }
                }
                requestTags.add(tag);
                currentRequestQueue.add(request);
            }
        }
    }

    /**
     * 负责封装结果内容, post给订阅者
     * @param action 任务标识
     * @param response 响应结果 
     */
    @Override
    public void onResult(int action, Object response)
    {
        synchronized (lock)
        {
            if (action == syncRequestId)
            {
                isSyncRequesting = false;
                Iterator<Request<?>> iterator = waitQueue.iterator();
                while(iterator.hasNext())
                {
                    Request<?> request = iterator.next();
                    if (request instanceof InfoResultRequest) {
                        InfoResultRequest infoResultRequest = (InfoResultRequest) request;
                       
                        // 更新正在等待的请求数据
                        infoResultRequest.updateRequest();
                        
                        if (isSyncRequesting)
                        {
                            continue;
                        }
                        else
                        {
                            if (infoResultRequest.isSyncRequest())
                            {
                                syncRequestId = infoResultRequest.getRequestId();
                                isSyncRequesting = true;
                            }
                        }
                    }
                    // 从缓存队列移除
                    iterator.remove();
                    
                    // 加入到请求队列
                    requestTags.add(request.getTag());
                    // 找出对应的请求队列
                    requestQueuePool.get(request.getVolleyTag()).add(request);
                }
            }
        }
        Message msg = new Message();
        msg.what = action;
        msg.obj = response;
        mEventBus.post(msg);
    }
    
    /**
     * EventBus订阅者事件通知的函数, UI线程
     * 
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
        mEventBus.post(msg);
    }
}
