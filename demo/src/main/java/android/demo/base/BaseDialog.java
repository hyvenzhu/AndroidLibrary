package android.demo.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import library.common.framework.ui.activity.presenter.DialogPresenter;
import library.common.framework.ui.activity.view.IDelegate;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public abstract class BaseDialog<T extends IDelegate> extends DialogPresenter<T> {
    int screenWidth;
    int screenHeight;
    float wPercent;
    float hPercent;
    int gravity = Gravity.CENTER_VERTICAL;
    int dimAmount = -1;
    
    @Override
    protected void onCreate() {
        super.onCreate();
        getScreenSize();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (wPercent > 0 && hPercent > 0) {
            Window win = getDialog().getWindow();
            WindowManager.LayoutParams params = win.getAttributes();
            params.gravity = gravity;
            params.width = (int) (screenWidth * wPercent);
            params.height = (int) (screenHeight * hPercent);
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            win.setAttributes(params);
        }
        
        if (dimAmount >= 0) {
            Window win = getDialog().getWindow();
            WindowManager.LayoutParams params = win.getAttributes();
            params.dimAmount = dimAmount;
            win.setAttributes(params);
        }
    }
    
    /**
     * 设置 Dialog 外边框颜色，0 则外边框完全透明
     *
     * @param dimAmount
     */
    public void setDimAmount(int dimAmount) {
        this.dimAmount = dimAmount;
    }
    
    public void setBackgroundDrawable(Drawable drawable) {
        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(drawable);
    }
    
    public void setBackgroundDrawableResource(@DrawableRes int resId) {
        Window win = getDialog().getWindow();
        win.setBackgroundDrawableResource(resId);
    }
    
    /**
     * 设置宽高比
     *
     * @param wPercent
     * @param hPercent
     */
    public void setPercent(float wPercent, float hPercent) {
        this.wPercent = wPercent;
        this.hPercent = hPercent;
    }
    
    /**
     * 设置宽高比
     *
     * @param wPercent
     * @param hPercent
     * @param gravity
     */
    public void setPercent(float wPercent, float hPercent, int gravity) {
        this.wPercent = wPercent;
        this.hPercent = hPercent;
        this.gravity = gravity;
    }
    
    void getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }
    
    @Override
    protected void onResponse(Message msg) {
        super.onResponse(msg);
        if (msg.obj instanceof InfoResult) {
            InfoResult infoResult = (InfoResult) msg.obj;
            if (infoResult.isSuccess()) {
                onSuccess(msg.what, infoResult.getData(), infoResult.getCode());
            } else {
                onFailure(msg.what, infoResult.getData(), infoResult.getCode(), infoResult.getErrmsg());
            }
        } else {
            onFailure(msg.what, msg.obj, null, NetworkError.errorMsg(getContext(), msg.obj));
        }
    }
    
    /**
     * 成功响应
     *
     * @param requestId    请求Id
     * @param response     响应结果
     * @param responseCode 响应码
     */
    protected void onSuccess(int requestId, Object response, String responseCode) {
    }
    
    /**
     * 失败响应
     *
     * @param requestId    请求Id
     * @param response     响应结果
     * @param responseCode 响应码
     * @param errmsg       错误信息
     */
    protected void onFailure(int requestId, Object response, String responseCode, String errmsg) {
    }
}
