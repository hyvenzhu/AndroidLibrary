package library.common.framework.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

/**
 * 可监听 X 或 Y 方向滚动比例
 *
 * @author hyvenzhu
 * @version 2022/8/19
 */
class NestedScrollViewObservable(context: Context, attributeSet: AttributeSet?) :
    NestedScrollView(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    /**
     * X 或 Y 方向滚动比例监听器
     */
    var scrollListener: ((scrollXPercent: Float, scrollYPercent: Float) -> Unit)? = null

    /**
     * 滚动比例参照 View
     */
    lateinit var refView: View

    override fun onScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)
        scrollListener?.invoke((scrollX.toFloat() / refView.width).coerceAtMost(1f), (scrollY.toFloat() / refView.height).coerceAtMost(1f))
    }
}