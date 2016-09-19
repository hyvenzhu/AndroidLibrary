package com.android.baseline.framework.ui.activity.base.helper;

import com.android.baseline.framework.logic.EventLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic帮助类
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/8/17 14:58]
 */
public class LogicHelper {
    private List<EventLogic> logics = new ArrayList<EventLogic>(); // 存储BaseLogic
    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     * @param logic
     * @param <T>
     * @return
     */
    public <T extends EventLogic> T registLogic(EventLogic logic)
    {
        logics.add(logic);
        return (T)logic;
    }

    /**
     * 解绑当前订阅者
     * @param iLogics
     */
    public void unregist(EventLogic... iLogics)
    {
        for(EventLogic iLogic : iLogics)
        {
            if (iLogic != null)
            {
                iLogic.unregisterAll();
            }
        }
    }

    /**
     * 解绑所有订阅者
     */
    public void unregistAll()
    {
        for(EventLogic iLogic : logics)
        {
            unregist(iLogic);
        }
    }
}
