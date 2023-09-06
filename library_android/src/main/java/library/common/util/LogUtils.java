package library.common.util;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Log 日志类工具
 * 详见：https://github.com/orhanobut/logger
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class LogUtils {

    private static final int FILE = 8;

    static {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return APKUtils.isDebug();
            }
        });
    }

    public static void tag(String tag) {
        Logger.t(tag);
    }

    public static void addDiskLogAdapter(Context context) {
        Logger.addLogAdapter(new DiskLogAdapter(CsvFormatStrategy.newBuilder().logStrategy(new CacheDiskLogStrategy(context)).build()) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return priority == FILE;
            }
        });
    }

    public static void toFile(String tag, String message) {
        Logger.log(FILE, tag, message, null);
    }

    public static void toFile(String message) {
        Logger.log(FILE, "toFile", message, null);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void e(String message, Object... args) {
        Logger.e(message, args);
    }

    public static void e(Throwable throwable) {
        Logger.e(throwable, "", "");
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }
}
