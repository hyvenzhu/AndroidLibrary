package library.common.framework.ui.activity.base.helper;

import library.common.framework.logic.EventLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic帮助类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class LogicHelper {
    /**
     * 存储BaseLogic
     */
    private List<EventLogic> logics = new ArrayList<EventLogic>();

    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     *
     * @param logic
     * @param <T>
     * @return
     */
    public <T extends EventLogic> T findLogic(EventLogic logic) {
        logics.add(logic);
        return (T) logic;
    }

    /**
     * 解绑当前订阅者
     *
     * @param iLogics
     */
    public void unregister(EventLogic... iLogics) {
        for (EventLogic iLogic : iLogics) {
            if (iLogic != null) {
                iLogic.unregister();
            }
        }
    }

    /**
     * 解绑所有订阅者
     */
    public void unregisterAll() {
        for (EventLogic iLogic : logics) {
            unregister(iLogic);
        }
    }
}
