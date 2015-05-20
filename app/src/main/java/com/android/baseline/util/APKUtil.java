package com.android.baseline.util;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.TextUtils;

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
    public File getDiskCacheDir(Context context, String uniqueName)
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

    /**
     * 组装参数
     * @param parameters
     * @return
     */
    public static String getParameters(Map<String, Object> parameters)
    {
        if (parameters == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Set<String> keys = parameters.keySet();
        Iterator<String> keysIt = keys.iterator();
        while (keysIt.hasNext())
        {
            String key = keysIt.next();
            if (!TextUtils.isEmpty(key))
            {
                Object value = parameters.get(key);
                if (value == null)
                {
                    value = "";
                }
                sb.append(key + "=" + value + "&");
            }
        }
        if (sb.length() > 0)
        {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }
}
