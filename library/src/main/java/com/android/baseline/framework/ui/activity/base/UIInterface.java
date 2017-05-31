package com.android.baseline.framework.ui.activity.base;

import com.android.baseline.framework.ui.adapter.extend.page.PageWrapper;

import java.util.List;

/**
 * UI接口定义
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-11-3]
 */
public interface UIInterface {
    /**
     * 显示Toast
     *
     * @param message
     */
    void showToast(CharSequence message);

    /**
     * 分页查询空数据提示语
     * @param pageWrapper
     * @param source
     * @param <T>
     */
    <T> void showPagingEmptyToast(PageWrapper pageWrapper, List<T> source);

    /**
     * 显示进度条, 可取消
     *
     * @param message
     */
    void showProgress(String message);

    /**
     * 显示并设置进度条是否可取消
     *
     * @param message
     * @param cancelable
     */
    void showProgress(String message, boolean cancelable);

    /**
     * 隐藏进度条
     */
    void hideProgress();
}
