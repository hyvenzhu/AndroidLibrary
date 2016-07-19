package com.android.baseline.framework.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.baseline.AppDroid;
import com.android.baseline.R;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.ui.base.BaseActivity;
import com.android.baseline.framework.ui.base.UIInterface;
import com.android.baseline.framework.ui.view.LoadingView;

/**
 * 基类Activity [主要提供对话框、进度条和其他有关UI才做相关的功能]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-15]
 */
public class BasicActivity extends BaseActivity implements UIInterface
{
    private final String TAG = "BasicActivity";
    /** 基类Toast */
    private Toast mToast;
    private Dialog progressDialog;
    protected boolean isPaused;
    protected boolean mIsNeedRefresh;
    
    /** 加载进度 */
    private LoadingView mLoadingView;
    /** 视图加载器 */
    protected LayoutInflater mInflater;

    /** 标题栏 */
    protected Button leftBtn;
    protected TextView titleTxt;
    protected Button rightBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppDroid.getInstance().uiStateHelper.addActivity(this);
    }

    @Override
    protected void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        toolbar.removeAllViews();
        // add custom title bar here
        mInflater = LayoutInflater.from(this);

        // 通用标题栏
        View commonTitle = mInflater.inflate(R.layout.layout_common_title, toolbar);
        leftBtn = (Button)commonTitle.findViewById(R.id.title_left_btn);
        titleTxt = (TextView)commonTitle.findViewById(R.id.title_txt);
        rightBtn = (Button)commonTitle.findViewById(R.id.title_right_btn);
    }

    /**
     * 设置标题栏属性
     * @param leftVisible 左侧按钮是否可见
     * @param resId 标题资源id
     * @param rightVisible 右侧按钮是否可见
     */
    protected void setTitleBar(boolean leftVisible, int resId, boolean rightVisible)
    {
        leftBtn.setVisibility(leftVisible? View.VISIBLE : View.INVISIBLE);
        titleTxt.setText(resId);
        rightBtn.setVisibility(rightVisible? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题栏属性
     * @param leftVisible 左侧按钮是否可见
     * @param title 标题
     * @param rightVisible 右侧按钮是否可见
     */
    protected void setTitleBar(boolean leftVisible, String title, boolean rightVisible)
    {
        leftBtn.setVisibility(leftVisible? View.VISIBLE : View.INVISIBLE);
        titleTxt.setText(title);
        rightBtn.setVisibility(rightVisible? View.VISIBLE : View.INVISIBLE);
    }

    protected void afterSetContentView()
    {
        mLoadingView = (LoadingView)findViewById(R.id.loading_view);
        if (mLoadingView != null)
        {
            mLoadingView.register(this);
        }
    }
    
    /**
     * 正在加载
     */
    protected void onLoading()
    {
        onLoading(R.string.app_name);
    }
    
    /**
     * 正在加载
     * @param obj
     */
    protected void onLoading(Object obj)
    {
        onLoading(R.string.app_name, obj);
    }
    
    /**
     * 正在加载
     * @param stringId 描述信息
     */
    protected void onLoading(int stringId)
    {
        onLoading(getResources().getString(stringId));
    }
    
    /**
     * 正在加载
     * @param stringId 描述信息
     * @param obj
     */
    public void onLoading(int stringId, Object obj)
    {
        onLoading(getResources().getString(stringId), obj);
    }
    
    /**
     * 正在加载
     * @param loadDesc 描述信息
     */
    protected void onLoading(String loadDesc)
    {
        mLoadingView.onLoading(loadDesc, null);
    }
    
    /**
     * 正在加载
     * @param loadDesc 描述信息
     * @param obj 传递的参数
     */
    public void onLoading(String loadDesc, Object obj)
    {
        mLoadingView.onLoading(loadDesc, obj);
    }
    
    /**
     * 失败
     */
    protected void onFailure()
    {
        onFailure(R.string.loading_failure);
    }
    
    /**
     * 失败
     * @param stringId 描述信息
     */
    protected void onFailure(int stringId)
    {
        onFailure(getResources().getString(stringId));
    }
    
    /**
     * 失败
     * @param errorDesc 描述信息
     */
    protected void onFailure(String errorDesc)
    {
        mLoadingView.onFailure(errorDesc);
    }
    
    /**
     * 成功
     */
    protected void onSuccess()
    {
        mLoadingView.onSuccess();
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
        showProgress(message, true);
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
    private Dialog createLoadingDialog(String msg)
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
    
    protected boolean checkResponse(Message msg)
    {
        return checkResponse(msg, null, null, true);
    }
    
    protected boolean checkResponse(Message msg, boolean tipError)
    {
        return checkResponse(msg, null, null, tipError);
    }
    
    protected boolean checkResponse(Message msg, String errorTip)
    {
        return checkResponse(msg, null, errorTip, true);
    }
    
    protected boolean checkResponse(Message msg, String successTip, String errorTip)
    {
        return checkResponse(msg, successTip, errorTip, true);
    }
    
    /**
     * 校验服务器响应结果
     * @param msg
     * @param successTip 成功提示
     * @param errorTip 失败提示    为空使用服务器信息或本地固定信息
     * @param tipError 是否提示错误信息
     * @return true 业务成功, false业务失败
     */
    protected boolean checkResponse(Message msg, String successTip, String errorTip, boolean tipError)
    {
        if (msg.obj instanceof InfoResult) 
        {
            InfoResult result = (InfoResult)msg.obj;
            if (result.isSuccess())
            {
                if (!TextUtils.isEmpty(successTip))
                {
                    showToast(successTip);
                }
                return true;
            }
            else
            {
                if (tipError)
                {
                    if (!TextUtils.isEmpty(errorTip))
                    {
                        showToast(errorTip);
                    }
                    else if (!TextUtils.isEmpty(result.getDesc()))
                    {
                        showToast(result.getDesc());
                    }
                    else
                    {
                        showToast(getString(R.string.requesting_failure));
                    }
                }
                return false;
            }
        }
        else
        {
            if (tipError)
            {
                if (!TextUtils.isEmpty(errorTip))
                {
                    showToast(errorTip);
                }
                else
                {
                    showToast(getString(R.string.requesting_failure));
                }
            }
            return false;
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        isPaused = false;
        if (mIsNeedRefresh)
        {
            mIsNeedRefresh = false;
        }
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
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        hideProgress();
        AppDroid.getInstance().uiStateHelper.removeActivity(this);
    }
}
