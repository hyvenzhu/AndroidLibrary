package com.android.baseline.framework.ui.base.helper;

import com.android.baseline.framework.logic.BaseLogic;
import com.android.baseline.framework.logic.ILogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic帮助类
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/8/17 14:58]
 */
public class LogicHelper {
    private List<BaseLogic> logics = new ArrayList<BaseLogic>(); // 存储BaseLogic
    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     * @param logic
     * @param <T>
     * @return
     */
    public <T extends BaseLogic> T registLogic(BaseLogic logic)
    {
        logics.add(logic);
        return (T)logic;
    }

    /**
     * 解绑当前订阅者
     * @param iLogics
     */
    public void unregist(ILogic... iLogics)
    {
        for(ILogic iLogic : iLogics)
        {
            if (iLogic != null)
            {
                iLogic.cancelAll();
                iLogic.unregisterAll();
            }
        }
    }

    /**
     * 解绑所有订阅者
     */
    public void unregistAll()
    {
        for(ILogic iLogic : logics)
        {
            unregist(iLogic);
        }
    }
}
