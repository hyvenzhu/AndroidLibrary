package com.android.baseline.util.crash;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.android.baseline.AppDroid;
import com.android.baseline.BuildConfig;
import com.android.baseline.util.APKUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * Crash日志保存至SD卡
 * @author hiphonezhu@gmail.com
 */
public class LogUtil {

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

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * 打印异常栈信息
     *
     * @param tag
     * @param e
     */
    static void e(String tag, Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        String errorMsg = new String(baos.toByteArray());

        if (BuildConfig.DEBUG) { // debug模式打印到LogCat
            Log.e(tag, errorMsg);
            storeLog(tag, errorMsg + LINE_SEPARATOR + LINE_SEPARATOR + collectClientInfo());
        } else { // Release模式存储到SD卡
            storeLog(tag, errorMsg + LINE_SEPARATOR + LINE_SEPARATOR + collectClientInfo());
        }
    }


    /**
     * 手机设备信息
     * @return
     */
    private static String collectClientInfo() {
        StringBuilder systemInfo = new StringBuilder();
        systemInfo.append("CLIENT-INFO");
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Id: ");
        systemInfo.append(Build.ID);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Display: ");
        systemInfo.append(Build.DISPLAY);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Product: ");
        systemInfo.append(Build.PRODUCT);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Device: ");
        systemInfo.append(Build.DEVICE);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Board: ");
        systemInfo.append(Build.BOARD);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("CpuAbility: ");
        systemInfo.append(Build.CPU_ABI);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Manufacturer: ");
        systemInfo.append(Build.MANUFACTURER);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Brand: ");
        systemInfo.append(Build.BRAND);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Model: ");
        systemInfo.append(Build.MODEL);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Type: ");
        systemInfo.append(Build.TYPE);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Tags: ");
        systemInfo.append(Build.TAGS);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("FingerPrint: ");
        systemInfo.append(Build.FINGERPRINT);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Version.Incremental: ");
        systemInfo.append(Build.VERSION.INCREMENTAL);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Version.Release: ");
        systemInfo.append(Build.VERSION.RELEASE);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("SDKInt: ");
        systemInfo.append(Build.VERSION.SDK_INT);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Version.CodeName: ");
        systemInfo.append(Build.VERSION.CODENAME);
        systemInfo.append(LINE_SEPARATOR);
        String clientInfomation = systemInfo.toString();
        systemInfo.delete(0, systemInfo.length());
        return clientInfomation;
    }

    /**
     * 将日志信息保存至SD卡
     *
     * @param tag LOG TAG
     * @param strErrMsg 保存的打印信息
     */
    public static void storeLog(String tag, String strErrMsg) {
        if (Environment
                .getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            File fileDir = new File(SAVE_LOG_DIR_PATH);
            // 判断目录是否已经存在
            if (!fileDir.exists()) {
                if (!fileDir.mkdirs()) {
                    return;
                }
            }
            File file = new File(SAVE_LOG_PATH);
            // 判断日志文件是否已经存在
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                }
            }
            try {
                // 输出
                FileOutputStream fos = new FileOutputStream(file,
                        true);
                PrintWriter out = new PrintWriter(fos);
                out.println(fmt.format(System.currentTimeMillis()) + "  >>"
                        + tag + "<<  " + strErrMsg + '\r');
                out.flush();
                out.close();
            } catch (FileNotFoundException e1) {
            } catch (Exception e) {
            }
        }
    }
}
