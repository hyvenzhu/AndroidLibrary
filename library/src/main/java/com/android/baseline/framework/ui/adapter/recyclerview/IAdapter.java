package com.android.baseline.framework.ui.adapter.recyclerview;

import java.util.List;

/**
 * Adapter Interface
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 15:48]
 */
public interface IAdapter<T> {

    void setDataSource(List<T> data);

    List<T> getDataSource();

    T getItem(int position);
}
