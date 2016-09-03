package com.android.baseline.framework.ui.base.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic帮助类
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/8/17 14:58]
 */
public class LogicHelper {
    private List<com.android.baseline.framework.logic.LogicHelper> logics = new ArrayList<com.android.baseline.framework.logic.LogicHelper>(); // 存储BaseLogic
    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     * @param logic
     * @param <T>
     * @return
     */
    public <T extends com.android.baseline.framework.logic.LogicHelper> T registLogic(com.android.baseline.framework.logic.LogicHelper logic)
    {
        logics.add(logic);
        return (T)logic;
    }

    /**
     * 解绑当前订阅者
     * @param iLogics
     */
    public void unregist(com.android.baseline.framework.logic.LogicHelper... iLogics)
    {
        for(com.android.baseline.framework.logic.LogicHelper iLogic : iLogics)
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
        for(com.android.baseline.framework.logic.LogicHelper iLogic : logics)
        {
            unregist(iLogic);
        }
    }
}
