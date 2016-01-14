package com.android.baseline.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
     * RL参数编码
     * @param params
     * @return
     */
    public String encodeParameters(Map<String, Object> params) {
        return encodeParameters(params, "utf-8");
    }

    /**
     * URL参数编码
     * @param params
     * @param paramsEncoding
     * @return
     */
    public String encodeParameters(Map<String, Object> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();

        try {
            Iterator var5 = params.entrySet().iterator();

            while(var5.hasNext()) {
                java.util.Map.Entry uee = (java.util.Map.Entry)var5.next();
                encodedParams.append(URLEncoder.encode((String)uee.getKey(), paramsEncoding));
                encodedParams.append('=');
                Object value = uee.getValue();
                if(value == null) {
                    value = "";
                }

                encodedParams.append(URLEncoder.encode(value.toString(), paramsEncoding));
                encodedParams.append('&');
            }

            return encodedParams.toString();
        } catch (UnsupportedEncodingException var7) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var7);
        }
    }
}
