package com.android.baseline.framework.log;

import android.os.Environment;

import com.android.baseline.AppDroid;
import com.android.baseline.util.APKUtil;

/**
 * API for logging the App running info into the sdcard files with size and
 * total number limited, which will automatically choose the right file to write
 * according to some conditions.
 * 
 * <p>
 * Generally, use the Log.v() Log.d() Log.i() Log.w() and Log.e() methods.
 * 
 * <p>
 * The order in terms of verbosity, from least to most is ERROR, WARN, INFO,
 * DEBUG, VERBOSE. Verbose should never be compiled into an application except
 * during development. Debug logs are compiled in but stripped at runtime.
 * Error, warning and info logs are always kept.
 * 
 * <p>
 * <b>Tip:</b> A good convention is to declare a <code>TAG</code> constant in
 * your class:
 * 
 * <pre>
 * private static final String TAG = &quot;MyActivity&quot;;
 * </pre>
 * 
 * and use that in subsequent calls to the log methods.
 * </p>
 * 
 * <p>
 * <b>Tip:</b> Don't forget that when you make a call like
 * 
 * <pre>
 * Log.v(TAG,
 *         &quot;index=&quot; + i);
 * </pre>
 * 
 * that when you're building the string to pass into Log.d, the compiler uses a
 * StringBuilder and at least three allocations occur: the StringBuilder itself,
 * the buffer, and the String object. Realistically, there is also another
 * buffer allocation and copy, and even more pressure on the gc. That means that
 * if your log message is filtered out, you might be doing significant work and
 * incurring significant overhead.
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-1-28]
 */
public final class Logger
{
    /**
     * log tag
     */
    public static final String TAG = "Logger";

    /**
     * log dir
     */
    public static final String LOG_DIR = Environment
            .getExternalStorageDirectory()
                .getAbsolutePath() + "/" + APKUtil.getPackageName(AppDroid.getInstance().getApplicationContext()) + "/log/";

    /**
     * log file suffix
     */
    public static final String LOG_FILE_SUFFIX = ".log";

    /**
     * log path
     */
    public static final String LOG_FILE_PATH = LOG_DIR + APKUtil.getPackageName(AppDroid.getInstance().getApplicationContext()) + "_v"
            + APKUtil.getVerName(AppDroid.getInstance().getApplicationContext()) + LOG_FILE_SUFFIX;

    /**
     * stack count
     */

    public static final int STACK_COUNT = 4;

    /**
     * file size limitation per log file
     */
    public static final long MAXSIZE_PERFILE = 1048576;

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * current log level
     */
    private static int currentLevel = VERBOSE;

    private Logger()
    {
    }

    /**
     * Low-level logging call.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param level level
     * @return The number of bytes written.
     */
    public static int println(int level, String tag, String msg)
    {
        int result = 0;
        if (isLoggable(level))
        {
            result = android.util.Log.println(level,
                    tag,
                    msg);
        }
        else
        {
            return result;
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            android.util.Log.w(TAG,
                    "SD Card is unavailable.");
            return result;
        }

        if (!LogCache.getInstance().isStarted())
        {
            startService();
        }

        if (isEnableToSave(level))
        {
            long id = Thread.currentThread().getId();
            String methodName = Thread.currentThread().getStackTrace()[STACK_COUNT].getMethodName();
            LogCache.getInstance().write(levelString(level),
                    tag,
                    msg,
                    id,
                    methodName);
        }

        return result;
    }

    private static String levelString(int level)
    {
        String msg = "";
        switch (level)
        {
            case Logger.VERBOSE:
                msg = "Ver";
            case Logger.DEBUG:
                msg = "Deb";
            case Logger.INFO:
                msg = "Inf";
            case Logger.WARN:
                msg = "War";
            case Logger.ERROR:
                msg = "Err";
            default:
                msg = "Deb";
        }
        return msg;
    }

    /**
     * Send a {@link #VERBOSE} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int v(String tag, String msg)
    {
        return println(VERBOSE,
                tag,
                msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int v(String tag, String msg, Throwable tr)
    {
        return println(VERBOSE,
                tag,
                msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send a {@link #DEBUG} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int d(String tag, String msg)
    {
        return println(DEBUG,
                tag,
                msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int d(String tag, String msg, Throwable tr)
    {
        return println(DEBUG,
                tag,
                msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send an {@link #INFO} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int i(String tag, String msg)
    {
        return println(INFO,
                tag,
                msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int i(String tag, String msg, Throwable tr)
    {
        return println(INFO,
                tag,
                msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int w(String tag, String msg)
    {
        return println(WARN,
                tag,
                msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int w(String tag, String msg, Throwable tr)
    {
        return println(WARN,
                tag,
                msg + '\n' + getStackTraceString(tr));
    }

    /**
     * 
     * Send a {@link #WARN} log message and log the exception.<BR>
     * [功能详细描述]
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int w(String tag, Throwable tr)
    {
        return println(WARN,
                tag,
                getStackTraceString(tr));
    }

    /**
     * Send an {@link #ERROR} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int e(String tag, String msg)
    {
        return println(ERROR,
                tag,
                msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int e(String tag, Throwable tr)
    {
        return println(ERROR,
                tag,
                getStackTraceString(tr));
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static int e(String tag, String msg, Throwable tr)
    {
        return println(ERROR,
                tag,
                msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     * 
     * @param tr An exception to log
     * @return The number of bytes written.
     */
    public static String getStackTraceString(Throwable tr)
    {
        return android.util.Log.getStackTraceString(tr);
    }

    /**
     * 
     * Checks to see whether or not a log for the specified tag is loggable at
     * the specified level.<BR>
     * [功能详细描述]
     * 
     * @param level level
     * @return The number of bytes written.
     */
    public static boolean isLoggable(int level)
    {
        return level >= currentLevel;
    }

    /**
     * 是否保存到文件 [保存w级别及以上的log]
     * 
     * @param level
     * @return
     */
    public static boolean isEnableToSave(int level)
    {
        return level >= Logger.WARN;
    }

    /**
     * 
     * try to start the service of logging into sdcard.
     */
    public static synchronized void startService()
    {
        LogCache.getInstance().start();
    }

    /**
     * 
     * stop the service of logging into sdcard.
     */
    public static synchronized void stopService()
    {
        LogCache.getInstance().stop();
    }

}
