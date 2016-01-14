package com.android.baseline.framework.ui.adapter;

import android.content.Context;

import com.android.baseline.framework.ui.BasicAdapter;

import java.util.List;
import java.util.Map;

/**
 * 分页适配器(pageIndex、pageSize模式)
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-09-29 21:54]
 */
public abstract class AbsPageAdapter<T> extends BasicAdapter<T> {
    private int currPageIndex; // 当前页下标
    private int pageSize; // 分页大小
    private int lastPageIndex; // 记录上一次的页下标
    private IPage iPage; // 分页参数回调接口
    private boolean isLoading; // 是否正在加载
    private Object lock = new Object(); // 锁

    public AbsPageAdapter(Context context, List<T> data, int resourceId, IPage iPage) {
        super(context, data, resourceId);
        this.iPage = iPage;
        initPageConfig();
    }

    public AbsPageAdapter(Context context, List<T> data, Map<Integer, Integer> itemTypeResourceMap, IPage iPage) {
        super(context, data, itemTypeResourceMap);
        this.iPage = iPage;
        initPageConfig();
    }

    /**
     * 初始化分页参数
     */
    private void initPageConfig()
    {
        currPageIndex = getStartPageIndex() - 1;
        lastPageIndex = currPageIndex;
        pageSize = getPageSize();
        isLoading = false;
    }

    /**
     * 返回起始页下标, 默认为0
     * @return
     */
    protected int getStartPageIndex()
    {
        return IPage.DEFAULT_START_PAGE_INDEX;
    }

    /**
     * 返回分页大小, 默认为10
     * @return
     */
    protected int getPageSize()
    {
        return IPage.DEFAULT_PAGE_SIZE;
    }

    /**
     * 当前是否是第一页数据
     * @return
     */
    public boolean isFirstPage()
    {
        return currPageIndex <= getStartPageIndex();
    }

    /**
     * 分页加载数据
     * [可能会抛出异常，请确认数据加载结束后，你已经调用了finishLoad(boolean success)方法]
     * @param isFirstPage true: 第一页  false: 下一页
     */
    public void loadPage(boolean isFirstPage)
    {
        synchronized (lock)
        {
            if (isLoading) // 如果正在加载数据，则抛出异常
            {
                throw new RuntimeException();
            }
            else
            {
                isLoading = true;
            }
        }
        if (isFirstPage)
        {
            currPageIndex = getStartPageIndex();
        }
        else
        {
            currPageIndex++;
        }
        iPage.load(currPageIndex, pageSize);
    }

    /**
     * 加载结束
     * @param success true：加载成功  false：失败(无数据)
     */
    private void finishLoad(boolean success)
    {
        synchronized (lock)
        {
            isLoading = false;
        }
        if (success)
        {
            lastPageIndex = currPageIndex;
        }
        else
        {
            currPageIndex = lastPageIndex;
        }
    }

    /**
     * 加载失败时调用
     */
    public void loadFailure()
    {
        finishLoad(false);
    }

    @Override
    public void setDataSource(List<T> data) {
        super.setDataSource(data);
        finishLoad(true);
    }

    /**
     * 增加数据
     * @param data
     */
    public final void addDataSource(List<T> data)
    {
        if (mData == null)
        {
            mData = data;
        }
        else
        {
            if (currPageIndex == getStartPageIndex())
            {
                mData.clear();
            }
            mData.addAll(data);
        }
        finishLoad(true);
    }

    /**
     * 分页参数回调接口
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
}
