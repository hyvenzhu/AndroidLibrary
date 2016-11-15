package com.android.baseline.util.crash;

import android.os.Environment;
import android.util.Log;

import com.android.baseline.AppDroid;
import com.android.baseline.util.APKUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * 日志的功能操作类 可将日志保存至SD卡
 *
 * @author hiphonezhu@gmail.com
 * @version [OApp, 2014-12-17]
 */
public class LogUtil {

    /**
     * 定义当前日志打印级别
     */
    private static int logLevel = 5;

    private static final int VERBOSE = 1;

    private static final int DEBUG = 2;

    private static final int INFO = 3;

    private static final int WARNING = 4;

    private static final int ERROR = 5;

    /**
     * 日志打印控制开关
     */

    private static boolean isPrintLog = true;
    /**
     * 是否保存至SD卡
     */
    private static boolean SAVE_TO_SD = false;

    private static boolean isPrintStackInfo = true;

    /**
     * 保存LOG日志的目录
     */
    private static final String SAVE_LOG_DIR_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/" + APKUtil.getPackageName(AppDroid.getInstance().getApplicationContext()) + "/log/";

    /**
     * 保存LOG日志的路径
     */
    private static final String SAVE_LOG_PATH = SAVE_LOG_DIR_PATH
            + APKUtil.getPackageName(AppDroid.getInstance().getApplicationContext()) + "_CrashException_v"
            + APKUtil.getVerName(AppDroid.getInstance().getApplicationContext()) + ".log";

    /**
     * 日志打印时间Format
     */
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 用于打印debug级的日志信息
     *
     * @param strModule LOG TAG
     * @param strErrMsg 打印信息
     */
    public static void d(String strModule, String strErrMsg) {
        if (DEBUG >= logLevel) {
            if (isPrintLog) {
                Log.d(strModule,
                        strErrMsg);
            }
            if (SAVE_TO_SD) {
                storeLog(strModule,
                        strErrMsg);
            }
        }
    }

    /**
     * 用于打印info级别的日志信息
     *
     * @param strModule LOG TAG
     * @param strErrMsg 打印信息
     */
    public static void i(String strModule, String strErrMsg) {
        if (INFO >= logLevel) {
            Log.i(strModule,
                    strErrMsg);
            if (SAVE_TO_SD) {
                storeLog(strModule,
                        strErrMsg);
            }
        }
    }

    /**
     * 用于打印warning级别的日志信息
     *
     * @param strModule LOG TAG
     * @param strErrMsg 打印信息
     */
    public static void w(String strModule, String strErrMsg) {
        if (WARNING >= logLevel) {
            if (isPrintLog) {
                Log.w(strModule,
                        strErrMsg);
            }
            if (SAVE_TO_SD) {
                storeLog(strModule,
                        strErrMsg);
            }
        }
    }

    /**
     * 用于打印verbose级别的日志信息
     *
     * @param strModule LOG TAG
     * @param strErrMsg 打印信息
     */
    public static void v(String strModule, String strErrMsg) {
        if (VERBOSE >= logLevel) {
            if (isPrintLog) {
                Log.v(strModule,
                        strErrMsg);
            }
            if (SAVE_TO_SD) {
                storeLog(strModule,
                        strErrMsg);
            }
        }
    }

    /**
     * 打印异常栈信息
     *
     * @param strModule
     * @param e
     */
    public static void e(String strModule, Exception e) {
        if (ERROR >= logLevel) {
            if (isPrintStackInfo) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
            storeLog(strModule,
                    e.getMessage());
        }
    }

    /**
     * 用于打印error级的日志信息
     *
     * @param strModule LOG TAG
     * @param strErrMsg 打印信息
     */
    public static void e(String strModule, String strErrMsg) {
        if (ERROR >= logLevel) {
            if (isPrintLog) {
                Log.e(strModule,
                        ">>" + strErrMsg + "<<");
            }
            storeLog(strModule,
                    strErrMsg);
        }
    }

    /**
     * 将日志信息保存至SD卡
     *
     * @param strModule LOG TAG
     * @param strErrMsg 保存的打印信息
     */
    public static void storeLog(String strModule, String strErrMsg) {
        if (Environment
                .getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            File fileDir = new File(SAVE_LOG_DIR_PATH);
            // 判断目录是否已经存在
            if (!fileDir.exists()) {
                if (!fileDir.mkdirs()) {
                    Log.e(strModule,
                            "Failed to create directory " + SAVE_LOG_DIR_PATH);
                    return;
                }
            }
            File file = new File(SAVE_LOG_PATH);
            // 判断日志文件是否已经存在
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                // 输出
                FileOutputStream fos = new FileOutputStream(file,
                        true);
                PrintWriter out = new PrintWriter(fos);
                out.println(fmt.format(System.currentTimeMillis()) + "  >>"
                        + strModule + "<<  " + strErrMsg + '\r');
                out.flush();
                out.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
