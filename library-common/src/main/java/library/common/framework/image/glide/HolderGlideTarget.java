package library.common.framework.image.glide;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import library.common.R;


/**
 * 支持占位图、背景的定制
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-18]
 */
public class HolderGlideTarget extends DrawableImageViewTarget {
    ImageView.ScaleType mScaleType;
    /**
     * ImageView 背景
     */
    @DrawableRes
    int bgRes;

    public HolderGlideTarget(ImageView view) {
        this(view, -1);
    }

    /**
     * @param view
     * @param bgRes ImageView 背景
     */
    public HolderGlideTarget(ImageView view, @DrawableRes int bgRes) {
        super(view);
        this.bgRes = bgRes;
        Object scaleType = view.getTag(R.id.com_scaleType);
        if (scaleType != null) {
            mScaleType = (ImageView.ScaleType) scaleType;
        } else {
            mScaleType = view.getScaleType();
            view.setTag(R.id.com_scaleType, mScaleType);
        }
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        super.onLoadStarted(new ColorDrawable(Color.TRANSPARENT));
        preLoad(placeholder);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        super.onLoadFailed(new ColorDrawable(Color.TRANSPARENT));
        preLoad(errorDrawable);
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        ImageView imageView = getView();
        imageView.setBackgroundColor(Color.TRANSPARENT);
        imageView.setScaleType(mScaleType);
        super.onResourceReady(resource, transition);
    }

    void preLoad(final Drawable preDrawable) {
        ImageView imageView = getView();
        if (bgRes != -1) {
            imageView.setBackgroundResource(bgRes);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageDrawable(preDrawable);
    }
}
