package library.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Intent跳转工具类
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */

public class IntentUtil {

    public static void startActivity(Activity activity, Class destClass) {
        startActivity(activity, destClass, null);
    }

    public static void startActivity(Activity activity, Class destClass, Bundle extras) {
        Intent intent = new Intent(activity, destClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent, activity);
    }

    public static void startActivity(Intent intent, Activity activity) {
        activity.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class destClass, int requestCode) {
        startActivityForResult(activity, destClass, requestCode, null);
    }

    public static void startActivityForResult(Activity activity, Class destClass, int requestCode, Bundle extras) {
        Intent intent = new Intent(activity, destClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivityForResult(intent, activity, requestCode);
    }

    public static void startActivityForResult(Intent intent, Activity activity, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class destClass, int requestCode) {
        startActivityForResult(fragment, destClass, requestCode, null);
    }

    public static void startActivityForResult(Fragment fragment, Class destClass, int requestCode, Bundle extras) {
        Intent intent = new Intent(fragment.getActivity(), destClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivityForResult(intent, fragment, requestCode);
    }

    public static void startActivityForResult(Intent intent, Fragment fragment, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startSingleTopActivity(Activity activity, Class destClass) {
        startSingleTopActivity(activity, destClass, null);
    }

    public static void startSingleTopActivity(Activity activity, Class destClass, Bundle extras) {
        Intent intent = new Intent(activity, destClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void startSingleTaskActivity(Context context, Class destClass) {
        startSingleTaskActivity(context, destClass, null);
    }

    public static void startSingleTaskActivity(Context context, Class destClass, Bundle extras) {
        Intent intent = new Intent(context, destClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
