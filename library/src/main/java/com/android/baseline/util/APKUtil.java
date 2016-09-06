package com.android.baseline.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import java.io.File;

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
    public static int getVerCode(Context context)
    {
        int verCode = -1;
        try
        {
            verCode = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode;
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
    public static String getVerName(Context context)
    {
        String verName = "";
        try
        {
            verName = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
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
    public static String getPackageName(Context context)
    {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获得磁盘缓存目录 [PS：应用卸载后会被自动删除]
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
        {
            cachePath = context.getApplicationContext().getExternalCacheDir().getPath();
        }
        else
        {
            cachePath = context.getApplicationContext().getFilesDir().getPath();
        }
        File dir = new File(cachePath + File.separator + uniqueName);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        return dir;
    }
}
