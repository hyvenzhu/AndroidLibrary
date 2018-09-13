package library.common.util;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Log 日志类工具
 * 详见：https://github.com/orhanobut/logger
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class LogUtil {

    static {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return APKUtil.isDebug();
            }
        });
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

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
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
