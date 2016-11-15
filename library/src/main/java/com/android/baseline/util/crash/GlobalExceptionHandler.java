package com.android.baseline.util.crash;

import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 全局异常处理
 *
 * @author hiphonezhu@gmail.com
 * @version [2014-6-25]
 */
public final class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

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
        // 将报错信息发送到指定的邮箱
        Thread sendMailThread = new Thread() {
            @Override
            public void run() {
                if (!handleException(throwable) && defaultHandler != null) {
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
    private boolean handleException(final Throwable throwable) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            throwable.printStackTrace(ps);
            String errorMsg = new String(baos.toByteArray());

            String content = errorMsg + LINE_SEPARATOR + LINE_SEPARATOR + collectClientInfo();
            LogUtil.e("GlobalExceptionHandler", content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Collect device info
     *
     * @return
     */
    private String collectClientInfo() {
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
}
