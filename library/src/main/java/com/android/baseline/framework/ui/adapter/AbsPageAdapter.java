package com.android.baseline.framework.ui.adapter;

import android.content.Context;

import com.android.baseline.framework.logic.page.IPage;
import com.android.baseline.framework.logic.page.Page1;
import com.android.baseline.framework.ui.BasicAdapter;

import java.util.List;
import java.util.Map;

/**
 * 分页适配器(pageIndex、pageSize模式)
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-09-29 21:54]
 */
public abstract class AbsPageAdapter<T> extends BasicAdapter<T> {
    Page1 page1;
    public AbsPageAdapter(Context context, List<T> data, int resourceId, Page1 page1) {
        super(context, data, resourceId);
        initPage(page1);
    }

    public AbsPageAdapter(Context context, List<T> data, Map<Integer, Integer> itemTypeResourceMap, Page1 page1) {
        super(context, data, itemTypeResourceMap);
        initPage(page1);
    }

    /**
     * 初始化分页参数
     */
    private void initPage(Page1 page1)
    {
        this.page1 = page1;
        page1.setStartPageIndex(getStartPageIndex())
                .setPageSize(getPageSize());
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
        return page1.isFirstPage();
    }

    /**
     * 分页加载数据
     * [可能会抛出异常，请确认数据加载结束后，你已经调用了finishLoad(boolean success)方法]
     * @param isFirstPage true: 第一页  false: 下一页
     */
    public void loadPage(boolean isFirstPage)
    {
        page1.loadPage(isFirstPage);
    }

    /**
     * 加载结束
     * @param success true：加载成功  false：失败(无数据)
     */
    public void finishLoad(boolean success)
    {
        page1.finishLoad(success);
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
            if (isFirstPage())
            {
                mData.clear();
            }
            mData.addAll(data);
        }
        finishLoad(true);
    }
}
