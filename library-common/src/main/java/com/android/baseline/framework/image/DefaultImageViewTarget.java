package com.android.baseline.framework.image;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * 占位图、失败图使用的 ScaleType：{@link ImageView.ScaleType.CENTER_INSIDE}
 * author : zhuhf
 * e-mail : hiphonezhu@gmail.com
 * time   : 2018/03/02
 * desc   :
 * version: 1.0
 */
public class DefaultImageViewTarget extends DrawableImageViewTarget {
    ImageView.ScaleType mScaleType;
    
    public DefaultImageViewTarget(ImageView view) {
        super(view);
        mScaleType = view.getScaleType();
    }
    
    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        getView().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        super.onLoadStarted(placeholder);
    }
    
    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        getView().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        super.onLoadFailed(errorDrawable);
    }
    
    
    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        if (mScaleType != null) {
            getView().setScaleType(mScaleType);
        }
        super.onResourceReady(resource, transition);
    }
}
