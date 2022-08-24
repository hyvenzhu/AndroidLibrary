package library.common.framework.ui.widget

import android.widget.TextView
import androidx.core.widget.doAfterTextChanged

/**
 * [TextView] 有输入内容时加粗；反之，不加粗。
 * 不要再 xml 设置 android:textStyle="bold"，否则无效
 */
fun TextView.fakeBoldByInput() {
    doAfterTextChanged {
        paint.isFakeBoldText = it.toString().isNotEmpty()
    }
}
