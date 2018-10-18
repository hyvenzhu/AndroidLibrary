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


/**
 * 定制 ImageView 的占位图显示的尺寸和样式，居中显示，支持 View 背景的定制
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-18]
 */
public class HolderGlideTarget extends DrawableImageViewTarget {
    ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER_CROP;
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
        imageView.setBackgroundResource(bgRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageDrawable(preDrawable);
    }
}
