package com.android.baseline.framework.ui.adapter;

/**
 * 分页参数回调接口
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-09-29 21:54]
 */
public interface IPage
{
    // 默认起始页下标
    int DEFAULT_START_PAGE_INDEX = 0;
    // 默认分页大小
    int DEFAULT_PAGE_SIZE = 10;

    /**
     * 加载分页数据
     * @param pageIndex 下标
     * @param pageSize 分页大小
     */
    void load(int pageIndex, int pageSize);
}
