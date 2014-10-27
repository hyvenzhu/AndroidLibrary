package com.android.baseline.framework.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baseline.R;
import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.ui.base.BaseActivity;

/**
 * 基类Activity [主要提供对话框、进度条和其他有关UI才做相关的功能]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class BasicActivity extends BaseActivity
{
    private final String TAG = "BasicActivity";
    /** 基类Toast */
    private Toast mToast;
    private Dialog progressDialog;
    protected boolean isPaused;
    protected boolean mIsNeedRefresh;
    /** 保存栈中的Activity */
    protected static List<Activity> activityStack = new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityStack.add(this);
        Logger.d(TAG,
                "onCreate");
    }

    /**
     * 移除栈中的Activity
     * 
     * @param stackIndex
     */
    public void finishActivity(int stackIndex)
    {
        Activity activity = activityStack.get(stackIndex);
        activity.finish();
        activityStack.remove(stackIndex);
    }

    public void finishAll()
    {
        for (Activity activity : activityStack)
        {
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 
     * 根据字符串 show toast<BR>
     * 
     * @param message 字符串
     */
    public void showToast(CharSequence message)
    {
        if (isPaused)
        {
            return;
        }
        if (mToast == null)
        {
            mToast = Toast.makeText(this,
                    message,
                    Toast.LENGTH_SHORT);
        }
        else
        {
            mToast.setText(message);
        }
        mToast.show();
    }

    public void showProgress(String message)
    {
        showProgress(message,
                true);
    }

    public void showProgress(String message, boolean cancelable)
    {
        if (progressDialog == null)
        {
            progressDialog = createLoadingDialog(message);
        }
        else if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        if (!TextUtils.isEmpty(message))
        {
            tipTextView.setText(message);
        }
        else
        {
            tipTextView.setText("数据加载中...");
        }
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    TextView tipTextView;

    /**
     * 得到自定义的progressDialog
     * 
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(String msg)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_loading,
                null);
        tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        if (!TextUtils.isEmpty(msg))
        {
            tipTextView.setText(msg);
        }
        Dialog loadingDialog = new Dialog(this,
                R.style.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(v);
        return loadingDialog;
    }

    public void hideProgress()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isPaused = false;
        Logger.d(TAG,
                "onResume");
        if (mIsNeedRefresh)
        {
            onRefresh();
            mIsNeedRefresh = false;
        }
    }

    /**
     * Activity onResume需要刷新会触发此方法
     */
    protected void onRefresh()
    {
    }

    /**
     * 设置重新启动后需要刷新
     * 
     * @param isNeedRefresh
     */
    protected void setNeedRefresh(boolean isNeedRefresh)
    {
        mIsNeedRefresh = isNeedRefresh;
    }

    /**
     * 此处重写默认情况下关闭loading dialog [子类希望改变此行为, 可以调用defaultHideDialog(false), 详见
     * {@link #defaultDialogHidden(boolean)}]
     * 
     * @param msg
     * @see com.android.baseline.framework.ui.base.BaseActivity#onResponse(android.os.Message)
     */
    public void onResponse(Message msg)
    {
        if (dialogHidden)
        {
            hideProgress();
        }
    }

    boolean dialogHidden = true;

    /**
     * 设置网络请求结束是否关闭对话框, 默认是关闭
     * 
     * @param hidden true关闭 false不关闭
     */
    protected void defaultDialogHidden(boolean hidden)
    {
        dialogHidden = hidden;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        isPaused = true;
        /**
         * 这里进行一些输入法的隐藏操作
         */
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null != imm && imm.isActive())
        {
            if (null != this.getCurrentFocus() && null != this.getCurrentFocus().getWindowToken())
            {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(),
                        0);
            }
        }
        Logger.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        hideProgress();
        activityStack.remove(this);
        Logger.d(TAG, "onDestroy");
    }
}
