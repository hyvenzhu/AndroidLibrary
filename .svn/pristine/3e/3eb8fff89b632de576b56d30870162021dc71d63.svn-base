package com.android.baseline.util;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import com.android.baseline.AppDroid;

/**
 * 基础工具类 [尽量减少类似Util的类存在]
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-8-29]
 */
public class APKUtil
{
    /**
     * 获得版本号
     * 
     * @return
     */
    public static int getVerCode()
    {
        int verCode = -1;
        try
        {
            Context appContext = AppDroid.getInstance().getApplicationContext();
            verCode = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(),
                    0).versionCode;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获得版本名称
     * 
     * @return
     */
    public static String getVerName()
    {
        String verName = "";
        try
        {
            Context appContext = AppDroid.getInstance().getApplicationContext();
            verName = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(),
                    0).versionName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获得APP包名
     * 
     * @return
     */
    public static String getPackageName()
    {
        Context appContext = AppDroid.getInstance().getApplicationContext();
        return appContext.getPackageName();
    }

    /**
     * 获得磁盘缓存目录 [PS：应用卸载后会被自动删除]
     * 
     * @param context
     * @return
     */
    public File getDiskCacheDir(Context context)
    {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
        {
            cachePath = context.getExternalCacheDir().getPath();
        }
        else
        {
            cachePath = context.getFilesDir().getPath();
        }
        return new File(cachePath);
    }
}
