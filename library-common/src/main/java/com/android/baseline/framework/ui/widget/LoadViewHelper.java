package com.android.baseline.framework.ui.widget;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baseline.R;

public class LoadViewHelper {
    
    private ViewGroupLayout helper;
    
    AnimationDrawable loadingAnimation;
    
    public LoadViewHelper(View view) {
        this(new ViewGroupLayout(view));
    }
    
    private LoadViewHelper(ViewGroupLayout helper) {
        this.helper = helper;
    }
    
    public void showError(String errorText, String buttonText, @DrawableRes int resId,
                          View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.com_view_load_error);
        TextView textView = (TextView) layout.findViewById(R.id.textView1);
        ImageView imageView = (ImageView) layout.findViewById(R.id.iv_img);
        textView.setText(errorText);
        imageView.setImageResource(resId);
        TextView button = (TextView) layout.findViewById(R.id.button1);
        if (TextUtils.isEmpty(buttonText)) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            button.setText(buttonText);
            button.setOnClickListener(onClickListener);
        }
        helper.showLayout(layout);
    }
    
    public void showEmpty(String emptyDes, int resId) {
        View layout = helper.inflate(R.layout.com_view_load_empty);
        Button button = (Button) layout.findViewById(R.id.button1);
        button.setText(emptyDes);
        if (resId > 0) {
            ImageView img = (ImageView) layout.findViewById(R.id.imageView);
            img.setImageResource(resId);
        }
        helper.showLayout(layout);
    }
    
    public void showLayout(View layout) {
        helper.showLayout(layout);
    }
    
    public void showLoading(String loadText) {
        View layout = helper.inflate(R.layout.com_view_load_loading);
        TextView textView = (TextView) layout.findViewById(R.id.textView1);
        textView.setText(loadText);
        helper.showLayout(layout);
    }
    
    public void restore() {
        helper.resetView();
        if (loadingAnimation != null) {
            loadingAnimation.stop();
            loadingAnimation = null;
        }
    }
}
