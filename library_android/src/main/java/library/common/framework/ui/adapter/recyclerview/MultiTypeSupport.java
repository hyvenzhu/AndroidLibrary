package library.common.framework.ui.adapter.recyclerview;

import androidx.annotation.LayoutRes;

/**
 * 多样式支持
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class MultiTypeSupport<T> {
    /**
     * 根据 {@param viewType} 返回不同布局
     *
     * @param viewType
     * @return
     */
    @LayoutRes
    public abstract int getLayoutId(int viewType);

    /**
     * 返回不同itemType
     *
     * @param item
     * @param position
     * @return
     */
    public abstract int getItemViewType(T item, int position);
}
