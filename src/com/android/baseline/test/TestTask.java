package com.android.baseline.test;

import com.android.baseline.framework.asyncquery.Task;
import com.android.baseline.framework.logic.InfoResult;
/**
 * 模拟耗时任务
 * @author hiphonezhu@gmail.com
 * @version [BaseLine_Android_V5, 2014-9-19]
 */
public class TestTask extends Task
{
    public TestTask(int taskId)
    {
        super(taskId);
    }

    @Override
    public Object doInBackground()
    {
        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // bean封装
        return new InfoResult.Builder()
                   .success(true)
                   .extraObj("Thread sleep 3 ms.").build();
    }
}
