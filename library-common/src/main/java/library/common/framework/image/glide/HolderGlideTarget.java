package library.common.framework.image.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;


/**
 * 定制 ImageView 的占位图显示的尺寸和样式，居中显示，支持 View 背景的定制
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-18]
 */
public class HolderGlideTarget extends DrawableImageViewTarget {
    ImageView.ScaleType mScaleType;
    /**
     * 占位图缩放系数，具体可以理解为加载图不超过 View 宽度或者高度的比例，范围：(0,1]。
     * 建议占位图做的稍微大一点，这样压缩不会太明显
     */
    float mScaleFactor = 0.4f;
    /**
     * ImageView 背景
     */
    @DrawableRes
    int bgRes;
    
    Drawable preDrawable;
    Drawable resource;
    
    public HolderGlideTarget(ImageView view) {
        this(view, -1, -1);
    }
    
    public HolderGlideTarget(ImageView view, float scaleFactor) {
        this(view, scaleFactor, -1);
    }
    
    public HolderGlideTarget(ImageView view, @DrawableRes int bgRes) {
        this(view, -1, bgRes);
    }
    
    /**
     * @param view
     * @param scaleFactor (0,1]
     * @param bgRes       ImageView 背景
     */
    public HolderGlideTarget(ImageView view, float scaleFactor, @DrawableRes int bgRes) {
        super(view);
        mScaleType = view.getScaleType();
        if (scaleFactor >= 0) {
            mScaleFactor = scaleFactor;
        }
        this.bgRes = bgRes;
    }
    
    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        super.onLoadStarted(null);
        preLoad(placeholder);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        super.onLoadFailed(null);
        preLoad(errorDrawable);
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        this.resource = resource;
        ImageView imageView = getView();
        imageView.setBackgroundColor(Color.TRANSPARENT);
        imageView.setScaleType(mScaleType);
        super.onResourceReady(resource, transition);
    }
    
    public void onSizeReady(int width, int height) {
        ImageView imageView = getView();
        imageView.setBackgroundResource(bgRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        
        Drawable drawable = preDrawable;
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        float scaleRatio = -1;
        if (width > height) {
            // ImageView 宽度大于高度，按照高度缩放
            if (drawableHeight > height * mScaleFactor) {
                // 图片大于 ImageView 高度的 * mScaleFactor，缩放
                scaleRatio = (height * mScaleFactor) / drawableHeight;
            }
        } else {
            // ImageView 宽度小于高度，按照宽度缩放
            if (drawableWidth > width * mScaleFactor) {
                // 图片大于 ImageView 宽度 * mScaleFactor，缩放
                scaleRatio = (width * mScaleFactor) / drawableWidth;
            }
        }
        if (scaleRatio != -1) {
            // 图片的目标宽度和高度
            float drawableScaleWidth = drawableWidth * scaleRatio;
            float drawableScaleHeight = drawableHeight * scaleRatio;
            imageView.setImageDrawable(zoomDrawable(drawable, (int) drawableScaleWidth, (int) drawableScaleHeight));
        } else {
            imageView.setImageDrawable(drawable);
        }
    }
    
    void preLoad(final Drawable preDrawable) {
        if (preDrawable == null) {
            return;
        }
        this.preDrawable = preDrawable;
        getSize(sizeReadyCallback);
    }
    
    SizeReadyCallback sizeReadyCallback = new SizeReadyCallback() {
        @Override
        public void onSizeReady(int width, int height) {
            if (resource != null) {
                setResource(resource);
            } else {
                HolderGlideTarget.this.onSizeReady(width, height);
            }
        }
    };
    
    static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
    
    static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldBmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(oldBmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newBmp);
    }
}
