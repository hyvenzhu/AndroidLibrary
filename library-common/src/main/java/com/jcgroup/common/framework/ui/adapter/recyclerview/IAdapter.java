package com.jcgroup.common.framework.ui.adapter.recyclerview;

import java.util.List;

/**
 * Adapter Interface
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public interface IAdapter<T> {

    void setDataSource(List<T> data);

    List<T> getDataSource();

    T getItem(int position);
}
