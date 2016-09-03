package com.android.baseline.framework.ui.base.helper;

import com.android.baseline.framework.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Task帮助类
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/8/17 14:58]
 */
public class TaskHelper {
    private List<Task> tasks = new ArrayList<Task>(); // 存储BaseLogic
    /**
     * 注册Task, Activity销毁时是自动取消解绑
     * @param task
     * @param <T>
     * @return
     */
    public <T extends Task> T registTask(Task task)
    {
        tasks.add(task);
        return (T)task;
    }

    /**
     * 解绑当前订阅者
     * @param tasks
     */
    public void unregist(Task... tasks)
    {
        for(Task task : tasks)
        {
            if (task != null)
            {
                task.unregister();
            }
        }
    }

    /**
     * 解绑所有订阅者
     */
    public void unregistAll()
    {
        for(Task task : tasks)
        {
            unregist(task);
        }
    }
}
