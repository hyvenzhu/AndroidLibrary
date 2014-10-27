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
    public Task(int taskId)
    {
        this.mTaskId = taskId;
    }
    
    @Override
    public void run()
    {
        final Object result = doInBackground();
        Message msg = new Message();
        msg.what = mTaskId;
        msg.obj = result;
        EventBus.getDefault().post(msg);
    }
}
