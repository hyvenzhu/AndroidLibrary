package com.android.baseline.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.baseline.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 自定义对话框
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/1/3 22:18]
 */
public class CustomDialog {
    private Context context;
    private @LayoutRes int contentView;
    private boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;
    private float wPercent = -1; // 宽度占屏幕宽度的百分比
    private float hPercent = -1f; // 高度占屏幕高度的百分比
    private DialogInterface.OnDismissListener onDismissListener;
    Dialog dialog;
    Map<Integer, OnClickListener> clickMap = new HashMap<>();

    public CustomDialog(Context context) {
        this.context = context;
    }

    public CustomDialog setContentView(@LayoutRes int contentView) {
        this.contentView = contentView;
        return this;
    }

    public CustomDialog onClick(@IdRes int clickId, OnClickListener onClickListener) {
        clickMap.put(clickId, onClickListener);
        return this;
    }

    public CustomDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public CustomDialog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public CustomDialog wPercentOfScreen(float wPercent) {
        this.wPercent = wPercent;
        return this;
    }

    public CustomDialog hPercentOfScreen(float hPercent) {
        this.hPercent = hPercent;
        return this;
    }

    public CustomDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public CustomDialog create() {
        dialog = new Dialog(context, R.style.CustomDialogStyle);
        dialog.setOnDismissListener(onDismissListener);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return this;
    }

    /**
     * show之后才能调用
     * @param viewId
     * @return
     */
    public View findViewById(int viewId) {
        return dialog.getWindow().findViewById(viewId);
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 显示对话框
     * @return
     */
    public Window show() {
        if (dialog == null) {
            create();
        }
        dialog.show();
        // 设置宽度
        final Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (wPercent != -1f) {
            wl.width = (int) (width * wPercent);
        }
        if (hPercent != -1f){
            wl.height = (int) (height * hPercent);
        }
        window.setAttributes(wl);
        window.setContentView(contentView);
        // 绑定点击事件
        Set<Integer> clickIds = clickMap.keySet();
        Iterator<Integer> clickIdsIterator = clickIds.iterator();
        while (clickIdsIterator.hasNext()) {
            final int clickId = clickIdsIterator.next();
            window.findViewById(clickId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickMap.get(clickId).onClick(window, dialog);
                }
            });
        }
        return window;
    }

    public interface OnClickListener{
        void onClick(Window window, Dialog dialog);
    }
}
