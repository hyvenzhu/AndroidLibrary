package com.android.baseline.framework.asyncquery;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单的耗时任务执行器
 * 
 * @author hiphonezhu@gmail.com
 * @version [CCEnglish, 2014-5-21]
 */
public class TaskExecutor
{
    private static ExecutorService workerThread = Executors.newCachedThreadPool();
    private static TaskExecutor sInstance;

    private TaskExecutor()
    {
    }

    public synchronized static TaskExecutor getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new TaskExecutor();
        }
        return sInstance;
    }

    public void execute(ITask task)
    {
        workerThread.execute(task);
    }
}
