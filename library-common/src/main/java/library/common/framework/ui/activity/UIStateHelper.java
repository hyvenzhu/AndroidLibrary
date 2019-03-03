package library.common.framework.ui.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护Activity和Fragment状态
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class UIStateHelper {
    /**
     * 保存栈中的Activity
     */
    public List<Activity> activityStack = new ArrayList<>();
    
    /**
     * Activity创建时加入栈中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }
    
    /**
     * Activity移除时从栈中删除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }
    
    /**
     * 关闭所有Activity
     */
    public void finishAll() {
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }
    
    /**
     * 返回栈顶Activity
     *
     * @return
     */
    public Activity getTopActivity() {
        int size = activityStack.size();
        return size > 0 ? activityStack.get(size - 1) : null;
    }
    
    /**
     * 返回栈底Activity
     * @return
     */
    public Activity getBottomActivity() {
        int size = activityStack.size();
        return size > 0 ? activityStack.get(0) : null;
    }
}
