package com.android.baseline.util.crash2email;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.android.baseline.util.MailUtil;

/**
 * 全局异常处理
 * @author hiphonezhu@gmail.com
 * @version [2014-6-25]
 */
public final class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler
{
    private Thread.UncaughtExceptionHandler defaultHandler;

    /**
     * 全局错误处理构造函数
     */
    public GlobalExceptionHandler()
    {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * 捕获到异常
     * @param thread 异常线程
     * @param throwable 异常信息
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable)
    {
        // 将报错信息发送到指定的邮箱
        Thread sendMailThread = new Thread()
        {
            @Override
            public void run()
            {
                if (!handleException(throwable) && defaultHandler != null)
                {
                    defaultHandler.uncaughtException(thread, throwable);
                }
            }
        };
        sendMailThread.start();
    }

    /**
     * 自定义错误处理、收集错误信息 、发送错误报告等操作均在此完成.
     * 
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable throwable)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            throwable.printStackTrace(ps);
            String errorMsg = new String(baos.toByteArray());
            MailUtil.sendMail(CrashMailProvider.getInstance().getMailInfo(errorMsg));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
