package library.common.framework.image.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.io.File;
import java.util.concurrent.ExecutionException;

import library.common.framework.image.Displayable;

/**
 * 使用 Glide 展示图片
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class GlideDisplayable implements Displayable {
    private DrawableImageViewTarget mTarget;
    RequestOptions mRequestOptions;
    RequestListener<Drawable> mListener;

    /**
     * use default DrawableImageViewTarget
     */
    public GlideDisplayable() {

    }

    /**
     * create with custom DrawableImageViewTarget
     *
     * @param target
     */
    public GlideDisplayable(DrawableImageViewTarget target) {
        mTarget = target;
    }

    public void setTarget(DrawableImageViewTarget target) {
        mTarget = target;
    }

    public void setRequestListener(@Nullable RequestListener<Drawable> listener) {
        mListener = listener;
    }

    /**
     * 使用 GlideOptions
     *
     * @param requestOptions
     * @return
     */
    public GlideDisplayable withOptions(RequestOptions requestOptions) {
        mRequestOptions = requestOptions;
        return this;
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(decorateUrl(url)).apply(mRequestOptions != null ? mRequestOptions : GlideOptions.withOptions(placeHolderId, errorHolderId)).into(getTarget(imageView));
    }

    @Override
    public void display(Context context, ImageView imageView, String url) {
        display(context, imageView, url, -1, -1);
    }

    @Override
    public void displayCircle(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(decorateUrl(url)).listener(mListener).apply(mRequestOptions != null ? mRequestOptions : GlideOptions.withCircleOptions(placeHolderId, errorHolderId)).into(getTarget(imageView));
    }

    @Override
    public void displayCircle(Context context, ImageView imageView, String url) {
        displayCircle(context, imageView, url, -1, -1);
    }


    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, Drawable drawable, int radius, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(drawable).listener(mListener).apply(mRequestOptions != null ? mRequestOptions : GlideOptions.withRoundedOptions(radius, placeHolderId, errorHolderId)).into(getTarget(imageView));
    }

    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, String url, int radius, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(decorateUrl(url)).listener(mListener).apply(mRequestOptions != null ? mRequestOptions : GlideOptions.withRoundedOptions(radius, placeHolderId, errorHolderId)).into(getTarget(imageView));
    }

    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, String url, int radius) {
        displayRoundedCorners(context, imageView, url, radius, -1, -1);
    }

    @Override
    public File download(Context context, String url) {
        try {
            return Glide.with(context).download(url).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    DrawableImageViewTarget getTarget(ImageView imageView) {
        return mTarget != null ? mTarget : new DrawableImageViewTarget(imageView);
    }


    @Override
    public String decorateUrl(String url) {
        return url;
    }

    @Override
    public void displayLocal(Context context, ImageView imageView, @Nullable File file) {
        if (mRequestOptions == null) {
            Glide.with(context).load(file).listener(mListener).into(getTarget(imageView));
        } else {
            Glide.with(context).load(file).listener(mListener).apply(mRequestOptions).into(getTarget(imageView));
        }
    }

    @Override
    public void displayLocal(Context context, ImageView imageView, @Nullable Drawable drawable) {
        if (mRequestOptions == null) {
            Glide.with(context).load(drawable).listener(mListener).into(getTarget(imageView));
        } else {
            Glide.with(context).load(drawable).listener(mListener).apply(mRequestOptions).into(getTarget(imageView));
        }
    }

    @Override
    public void displayLocal(Context context, ImageView imageView, @Nullable Integer resourceId) {
        if (mRequestOptions == null) {
            Glide.with(context).load(resourceId).listener(mListener).into(getTarget(imageView));
        } else {
            Glide.with(context).load(resourceId).listener(mListener).apply(mRequestOptions).into(getTarget(imageView));
        }
    }

    @Override
    public void displayGif(Context context, ImageView imageView, @Nullable Integer resourceId) {
        if (mRequestOptions == null) {
            Glide.with(context).asGif().load(resourceId).into(imageView);
        } else {
            Glide.with(context).asGif().load(resourceId).apply(mRequestOptions).into(imageView);
        }
    }
}
