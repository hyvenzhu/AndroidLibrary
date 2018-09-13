package library.common.framework.image.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import library.common.framework.image.Displayable;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * 使用 Glide 展示图片
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class GlideDisplayable implements Displayable {
    private DrawableImageViewTarget mTarget;
    RequestOptions mRequestOptions;
    
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
        Glide.with(context).load(decorateUrl(url)).apply(GlideOptions.withOptions(placeHolderId, errorHolderId)).into(getTarget(imageView));
    }
    
    @Override
    public void display(Context context, ImageView imageView, String url) {
        display(context, imageView, url, -1, -1);
    }
    
    @Override
    public void displayCircle(Context context, ImageView imageView, String url, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(decorateUrl(url)).apply(GlideOptions.withCircleOptions(placeHolderId, errorHolderId)).into(imageView);
    }
    
    @Override
    public void displayCircle(Context context, ImageView imageView, String url) {
        displayCircle(context, imageView, url, -1, -1);
    }


    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, Drawable drawable, int radius, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(drawable).apply(GlideOptions.withRoundedOptions(radius, placeHolderId, errorHolderId)).into(getTarget(imageView));
    }
    
    @Override
    public void displayRoundedCorners(Context context, ImageView imageView, String url, int radius, int placeHolderId, int errorHolderId) {
        Glide.with(context).load(decorateUrl(url)).apply(GlideOptions.withRoundedOptions(radius, placeHolderId, errorHolderId)).into(getTarget(imageView));
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
            Glide.with(context).load(file).into(getTarget(imageView));
        } else {
            Glide.with(context).load(file).apply(mRequestOptions).into(getTarget(imageView));
        }
    }
    
    @Override
    public void displayLocal(Context context, ImageView imageView, @Nullable Drawable drawable) {
        if (mRequestOptions == null) {
            Glide.with(context).load(drawable).into(getTarget(imageView));
        } else {
            Glide.with(context).load(drawable).apply(mRequestOptions).into(getTarget(imageView));
        }
    }
    
    @Override
    public void displayLocal(Context context, ImageView imageView, @Nullable Integer resourceId) {
        if (mRequestOptions == null) {
            Glide.with(context).load(resourceId).into(getTarget(imageView));
        } else {
            Glide.with(context).load(resourceId).apply(mRequestOptions).into(getTarget(imageView));
        }
    }
}
