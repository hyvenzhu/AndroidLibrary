package library.common.framework.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import library.common.R;

/**
 * 固定比例的容器
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-09]
 */
public class FixedRatioLayout extends FrameLayout {
    private float mHeightRatio = -1f;
    private float mWidthRatio = -1f;
    
    public FixedRatioLayout(Context context) {
        super(context);
    }
    
    public FixedRatioLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }
    
    public FixedRatioLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.com_FixedRatioLayout);
        if (a.hasValue(R.styleable.com_FixedRatioLayout_com_heightRatio)) {
            mHeightRatio = a.getFloat(R.styleable.com_FixedRatioLayout_com_heightRatio, 1f / 1f);
        } else if (a.hasValue(R.styleable.com_FixedRatioLayout_com_widthRatio)) {
            mWidthRatio = a.getFloat(R.styleable.com_FixedRatioLayout_com_widthRatio, 1f / 1f);
        }
        a.recycle();
    }
    
    @UiThread
    public void setHeightRatio(float ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    @UiThread
    public void setWidthRatio(float ratio) {
        if (ratio != mWidthRatio) {
            mWidthRatio = ratio;
            requestLayout();
        }
    }

    public float getHeightRatio() {
        return mHeightRatio;
    }

    public float getWidthRatio() {
        return mWidthRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mHeightRatio > 0) {
            height = (int) (width * mHeightRatio);
        } else if (mWidthRatio > 0) {
            width = (int) (height * mWidthRatio);
        }
        setMeasuredDimension(width, height);
        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
