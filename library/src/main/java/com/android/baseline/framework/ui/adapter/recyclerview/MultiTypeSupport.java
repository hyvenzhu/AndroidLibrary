package com.android.baseline.framework.ui.adapter.recyclerview;

import android.support.annotation.LayoutRes;

/**
 * 多样式支持
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 14:11]
 */
public abstract class MultiTypeSupport<T> {
    /**
     * 根据itemType返回不同布局
     *
     * @param itemType
     * @return
     */
    @LayoutRes
    public abstract int getLayoutId(int itemType);

    /**
     * 返回不同itemType
     *
     * @param item
     * @param position
     * @return
     */
    public abstract int getItemViewType(T item, int position);
}
