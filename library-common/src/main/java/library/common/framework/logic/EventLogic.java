package library.common.framework.logic;

import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 维护Logic的订阅者,以及提供取消订阅等方法
 *
 * @version [AndroidLibrary, 2018-3-6]
 */
public class EventLogic {
    WeakReference<LogicCallback> callbackReference;
    
    public EventLogic() {
        this(null);
    }
    
    /**
     * subscriber 需要实现 {@link LogicCallback} 接口
     *
     * @param subscriber
     */
    public EventLogic(Object subscriber) {
        if (subscriber != null && subscriber instanceof LogicCallback) {
            this.callbackReference = new WeakReference<>((LogicCallback) subscriber);
        } else if (subscriber != null) {
            throw new IllegalArgumentException("subscriber must implements LogicCallback interface");
        }
    }
    
    /**
     * 负责封装结果内容, 回调给订阅者
     *
     * @param what 任务标识
     * @param obj  响应结果
     */
    public void onResult(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        if (callbackReference != null && callbackReference.get() != null) {
            callbackReference.get().call(msg);
        }
    }
    
    public void unregister() {
        if (callbackReference != null) {
            callbackReference.clear();
            callbackReference = null;
        }
    }
}
