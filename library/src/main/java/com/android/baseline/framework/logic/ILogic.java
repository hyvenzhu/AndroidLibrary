package com.android.baseline.framework.logic;
/**
 * 业务逻辑EventBus事件定义注册, 解绑订阅者
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public interface ILogic
{
    /**
     * 注册一个订阅者
     * 
     * @param receiver
     */
    void register(Object receiver);

    /**
     * 解绑一个订阅者
     * 
     * @param receiver
     */
    void unregister(Object receiver);

    /**
     * 解绑所有订阅者
     */
    void unregisterAll();
    
    /**
     * 取消某一个网络请求
     * @param tag 某次请求的唯一标识
     */
    void cancel(Object tag);
    
    /**
     * 取消所有网络请求
     */
    void cancelAll();
    
    /**
     * 封装结果内容, post给订阅者
     * @param action 
     * @param response
     */
    void onResult(int action, Object response);
}
