package com.android.baseline.framework.asyncquery;


import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Simple background task executor
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-5-21]
 */
public class TaskExecutor
{
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory  sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(10);
    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * An {@link Executor} that executes tasks one at a time in serial
     * order.  This serialization is global to a particular process.
     */
    public static final Executor SERIAL_EXECUTOR = Build.VERSION.SDK_INT >= 11 ? new SerialExecutor() :
            Executors.newSingleThreadExecutor(sThreadFactory);

    /** Fixed thread spool executor*/
    public static final Executor DUAL_THREAD_EXECUTOR =
            Executors.newFixedThreadPool(2, sThreadFactory);
    
    /** default executor*/
    public static final Executor CACHED_EXECUTOR = Executors.newCachedThreadPool();

    private static volatile Executor sDefaultExecutor = CACHED_EXECUTOR;
    
    @TargetApi(11)
    private static class SerialExecutor implements Executor {
        final LinkedList<Runnable> mTasks = new LinkedList<Runnable>();
        Runnable mActive;

        @Override
        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }

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

    /**
     * Execute a runnable using default executor
     * @param task
     */
    public void execute(ITask task)
    {
        execute(task, null);
    }
    
    /**
     * Execute a runnable using custom executor
     * @param task
     * @param executor
     */
    public void execute(ITask task, Executor executor)
    {
        if (executor == null)
        {
            sDefaultExecutor.execute(task);
        }
        else
        {
            executor.execute(task);
        }
    }
}
