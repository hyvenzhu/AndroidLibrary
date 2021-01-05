package library.common.framework.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 圆形 ImageView
 *
 * @author zhuhf
 * @version 2020/9/15
 */
public class CircleImageView extends RoundImageView {
    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDrawCircle();
    }
}
