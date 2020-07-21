package library.common.framework.ui.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 加载、错误、空数据、Toast、模态（非模态）对话框辅助类
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-21]
 */
public abstract class AbstractLoadHelper {
    private ViewSwitcher vs;
    LayoutInflater inflater;
    protected Context mContext;
    
    /**
     * @param view 内容布局
     */
    public AbstractLoadHelper(View view) {
        vs = new ViewSwitcher(view);
        inflater = LayoutInflater.from(view.getContext());
        mContext = view.getContext();
    }
    
    /**
     * 加载视图
     *
     * @param loadText
     * @param loadRes
     * @param bgRes
     * @return
     */
    public abstract View loadingView(String loadText, @DrawableRes int loadRes, @DrawableRes int bgRes);
    
    /**
     * 错误视图
     *
     * @param errorText
     * @param errorRes
     * @param showButton
     * @param buttonText
     * @param bgRes
     * @param onClickListener
     * @return
     */
    public abstract View errorView(String errorText, @DrawableRes int errorRes, boolean showButton, String buttonText,
                                   @DrawableRes int bgRes, View.OnClickListener onClickListener);
    
    /**
     * 空视图
     *
     * @param emptyText
     * @param emptyRes
     * @param buttonText
     * @param bgResId
     * @param onClickListener
     * @return
     */
    public abstract View emptyView(String emptyText, @DrawableRes int emptyRes, String buttonText,
                                   @DrawableRes int bgResId, View.OnClickListener onClickListener);
    
    /**
     * 显示默认 View
     */
    public void hide() {
        vs.reset();
    }
    
    /**
     * 显示指定的 View
     *
     * @param view
     */
    public void show(View view) {
        vs.show(view);
    }
    
    public View inflate(@LayoutRes int resource) {
        return inflater.inflate(resource, null);
    }
    
    public Resources getResources() {
        return mContext.getResources();
    }
    
    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     */
    public abstract void showProgress(String message, boolean cancelable);

    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     * @param cancelListener
     */
    public abstract void showProgress(String message, boolean cancelable, DialogInterface.OnCancelListener cancelListener);

    /**
     * 隐藏加载框
     */
    public abstract void hideProgress();
    
    /**
     * Toast
     *
     * @param message
     */
    public abstract void showToast(CharSequence message);
}
