package com.android.baseline.util.crash;

/**
 * 全局异常处理
 *
 * @author hiphonezhu@gmail.com
 * @version [2014-6-25]
 */
public final class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultHandler;

    /**
     * 全局错误处理构造函数
     */
    public GlobalExceptionHandler() {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * 捕获到异常
     *
     * @param thread    异常线程
     * @param throwable 异常信息
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        if (!handleException(throwable) && defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 自定义错误处理、收集错误信息 、发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable throwable) {
        try {
            LogUtil.e("UncaughtException", throwable);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
