package library.common.framework.ui.widget;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import library.common.R;


/**
 * 默认加载布局
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-21]
 */
public class DefaultLoadHelper extends AbstractLoadHelper {
    CustomDialog customDialog;
    TextView tipTextView;
    
    public DefaultLoadHelper(View view) {
        super(view);
    }
    
    @Override
    public View loadingView(String loadText, int loadRes, int bgRes) {
        View layout = inflate(R.layout.com_load_loading);
        TextView textView = layout.findViewById(R.id.textView1);
        if (bgRes > 0) {
            layout.setBackgroundResource(bgRes);
        }
        
        if (!TextUtils.isEmpty(loadText)) {
            textView.setText(loadText);
        } else {
            textView.setText(R.string.com_prompt_loading);
        }
        return layout;
    }
    
    @Override
    public View errorView(String errorText, int errorRes, String buttonText, int bgRes, View.OnClickListener onClickListener) {
        View layout = inflate(R.layout.com_load_error);
        TextView textView = layout.findViewById(R.id.textView1);
        ImageView imageView = layout.findViewById(R.id.iv_img);
        if (bgRes > 0) {
            layout.setBackgroundResource(bgRes);
        }
        if (!TextUtils.isEmpty(errorText)) {
            textView.setText(errorText);
        } else {
            textView.setText(R.string.com_loading_error_txt);
        }
        if (errorRes > 0) {
            imageView.setImageResource(errorRes);
        } else {
            imageView.setImageResource(R.drawable.com_ic_loading_error);
        }
        TextView button = layout.findViewById(R.id.button1);
        if (!TextUtils.isEmpty(buttonText)) {
            button.setText(buttonText);
        } else {
            button.setText(R.string.com_reload);
        }
        button.setOnClickListener(onClickListener);
        return layout;
    }
    
    @Override
    public View emptyView(String emptyDes, int emptyRes, String buttonText, int bgResId, View.OnClickListener onClickListener) {
        View layout = inflate(R.layout.com_load_empty);
        Button button = layout.findViewById(R.id.button1);
        if (!TextUtils.isEmpty(emptyDes)) {
            button.setText(emptyDes);
        } else {
            button.setText(R.string.com_nodata);
        }
        
        if (bgResId > 0) {
            layout.setBackgroundResource(bgResId);
        }
        ImageView img = layout.findViewById(R.id.imageView);
        if (emptyRes > 0) {
            img.setImageResource(emptyRes);
        } else {
            img.setImageResource(R.drawable.com_nodata);
        }
        layout.setOnClickListener(onClickListener);
        return layout;
    }
    
    
    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     */
    @Override
    public void showProgress(String message, boolean cancelable) {
        if (customDialog == null) {
            customDialog = new CustomDialog(mContext).setContentView(R.layout.com_dialog_loading)
                    .setCancelable(cancelable)
                    .setCanceledOnTouchOutside(false)
                    .create();
        } else {
            customDialog.dismiss();
        }
        customDialog.setCancelable(cancelable);
        customDialog.show();
        
        tipTextView = customDialog.findViewById(R.id.tipTextView);
        
        if (!TextUtils.isEmpty(message)) {
            tipTextView.setText(message);
        } else {
            tipTextView.setText("加载中...");
        }
    }
    
    /**
     * 隐藏加载框
     */
    @Override
    public void hideProgress() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }
    
    /**
     * Toast
     *
     * @param message
     */
    @Override
    public void showToast(CharSequence message) {
        ToastHelper.showToast(mContext, message);
    }
}
