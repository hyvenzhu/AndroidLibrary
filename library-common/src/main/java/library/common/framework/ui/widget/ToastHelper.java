package library.common.framework.ui.widget;

import android.content.Context;
import android.view.Gravity;

/**
 * Toast 工具类
 *
 * @author zhuhf
 * @version [AndroidLibrary-5.10, 2018-05-13]
 */
public class ToastHelper {
    private static android.widget.Toast mToast;
    /**
     * Toast
     *
     * @param message
     */
    public static void showToast(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(context.getApplicationContext(),
                    message,
                    android.widget.Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    public static void showToast(Context context, int message) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(context.getApplicationContext(),
                    context.getString(message),
                    android.widget.Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }
}
