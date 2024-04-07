package library.common.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.FileProvider;

import java.io.File;

import library.common.App;

/**
 * 基础工具类 [尽量减少类似Util的类存在]
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class APKUtils {
    /**
     * 获得版本号
     *
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获得版本名称
     *
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获得APP包名
     *
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    public static String getAuthority(Context context) {
        return getPackageName(context) + ".android7.fileprovider";
    }

    /**
     * 返回应用缓存目录
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File dir = context.getApplicationContext().getExternalCacheDir();
            if (dir == null) {
                dir = context.getApplicationContext().getCacheDir();
            }
            cachePath = dir.getPath();
        } else {
            File dir = context.getApplicationContext().getCacheDir();
            if (dir == null) {
                return null;
            }
            cachePath = dir.getPath();
        }
        File dir;
        if (TextUtils.isEmpty(uniqueName)) {
            dir = new File(cachePath);
        } else {
            dir = new File(cachePath + File.separator + uniqueName);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 返回应用缓存目录
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskFilesDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File dir = context.getApplicationContext().getExternalFilesDir(null);
            if (dir == null) {
                dir = context.getApplicationContext().getFilesDir();
            }
            cachePath = dir.getPath();
        } else {
            File dir = context.getApplicationContext().getFilesDir();
            if (dir == null) {
                return null;
            }
            cachePath = dir.getPath();
        }
        File dir;
        if (TextUtils.isEmpty(uniqueName)) {
            dir = new File(cachePath);
        } else {
            dir = new File(cachePath + File.separator + uniqueName);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * pix转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 7.0目录权限适配
     *
     * @param context
     * @param inputFile
     * @return
     */
    public static Uri getSupportUri(Context context, File inputFile) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(App.getInstance().getApplicationContext(), context.getPackageName() + ".android7.fileprovider", inputFile);
        } else {
            uri = Uri.fromFile(inputFile);
        }
        return uri;
    }

    /**
     * 7.0文件权限适配
     *
     * @param intent
     * @param writeAble
     * @return
     */
    public static Intent getSupportIntent(Intent intent, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            // 授予目录临时共享权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        return intent;
    }

    /**
     * 隐藏输入法
     *
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && imm.isActive()) {
            if (null != activity.getCurrentFocus() && null != activity.getCurrentFocus().getWindowToken()) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(),
                        0);
            }
        }
    }

    /**
     * 隐藏输入法
     *
     * @param context
     * @param focusedView
     */
    public static void hideSoftInputFromWindow(Context context, View focusedView) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && imm.isActive()) {
            if (null != focusedView.getWindowToken()) {
                imm.hideSoftInputFromWindow(focusedView.getApplicationWindowToken(),
                        0);
            }
        }
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param editText
     */
    public static void showSoftInput(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    private static Boolean isDebug = null;

    public static boolean isDebug() {
        return isDebug == null ? false : isDebug.booleanValue();
    }

    /**
     * Sync lib debug with app's debug value. Should be called in module Application
     *
     * @param context
     */
    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    /**
     * 设置状态栏可见性(不建议隐藏导航栏，参考各大主流App)
     *
     * @param activity
     * @param show
     */
    public static void setStatusBarVisible(Activity activity, boolean show) {
        if (show) {
            final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 是否安装了某个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * 重启应用
     *
     * @param context
     * @param intent
     */
    public static void triggerRebirth(Context context, Intent intent) {
        Intent targetIntent = intent == null? context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()) : intent;
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(targetIntent);
        Runtime.getRuntime().exit(0);
    }

    /**
     * 返回状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
