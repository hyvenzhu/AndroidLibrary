package com.android.baseline.framework.asyncquery;

import android.os.Message;
import de.greenrobot.event.EventBus;

/**
 * 任务父类, 子类继承并实现doInBackground()方法, 在其中执行耗时操作
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-5-21]
 */
public abstract class Task implements ITask
{
    private int mTaskId;
    private EventBus eventBus;
    public Task(int taskId)
    {
        this(taskId, null);
        this.eventBus = new EventBus();
    }
    
    public Task(int taskId, EventBus eventBus)
    {
        this.mTaskId = taskId;
        if (eventBus == null)
        {
            eventBus = new EventBus();
        }
        this.eventBus = eventBus;
    }
    
    @Override
    public void run()
    {
        final Object result = doInBackground();
        Message msg = new Message();
        msg.what = mTaskId;
        msg.obj = result;
        eventBus.post(msg);
    }
}
