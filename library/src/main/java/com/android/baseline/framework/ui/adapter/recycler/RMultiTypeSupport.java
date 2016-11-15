package com.android.baseline.framework.ui.adapter.recycler;

import com.android.baseline.framework.ui.adapter.MultiTypeSupport;

/**
 * RecycleView多样式支持(getViewTypeCount()重写一下而已)
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 14:11]
 */
public abstract class RMultiTypeSupport<T> extends MultiTypeSupport<T> {
    @Override
    public int getViewTypeCount() {
        return 0;
    }
}
