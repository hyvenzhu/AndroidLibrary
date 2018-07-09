package com.jcgroup.common.util;

/**
 * @author zhuhf
 * @version [DX-AndroidLibrary, 2018-03-14]
 */
public interface Callback<T> {
    /**
     * call the observer
     *
     * @param data
     */
    void call(T data);
}
