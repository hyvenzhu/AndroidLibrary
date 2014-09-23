package com.android.baseline.framework.logic;
/**
 * 业务逻辑EventBus事件定义注册, 解绑订阅者
 * 
 * @author hiphonezhu@gmail.com
 * @version [BaseLine_Android_V5, 2014-9-15]
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
     * 封装结果内容, post给订阅者
     * @param action 
     * @param response
     */
    void onResult(int action, Object response);
}
