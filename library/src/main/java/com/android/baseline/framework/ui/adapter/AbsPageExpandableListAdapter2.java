package com.android.baseline.framework.ui.adapter;

import android.content.Context;

import com.android.baseline.framework.ui.BasicExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * 分页适配器(start、end模式)
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-09-29 21:54]
 */
public abstract class AbsPageExpandableListAdapter2<K, V> extends BasicExpandableListAdapter<K, V> {
    private int startIndex; // 起始下标
    private int lastStartIndex; // 记录上一次的起始下标
    private int pageSize; // 分页大小
    private IPage2 iPage; // 分页参数回调接口
    private boolean isLoading; // 是否正在加载
    private Object lock = new Object(); // 锁

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children, int groupResourceId, int childrenResourceId, IPage2 iPage) {
        super(context, group, children, groupResourceId, childrenResourceId);
        this.iPage = iPage;
        initPageConfig();
    }

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children,
                                        Map<Integer, Integer> groupItemTypeResourceMap,
                                        Map<Integer, Integer> childItemTypeResourceMap, IPage2 iPage) {
        super(context, group, children, groupItemTypeResourceMap, childItemTypeResourceMap);
        this.iPage = iPage;
        initPageConfig();
    }

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children,
                                        int groupResourceId,
                                        Map<Integer, Integer> childItemTypeResourceMap, IPage2 iPage) {
        super(context, group, children, groupResourceId, childItemTypeResourceMap);
        this.iPage = iPage;
        initPageConfig();
    }

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children,
                                        Map<Integer, Integer> groupItemTypeResourceMap,
                                        int childrenResourceId, IPage2 iPage) {
        super(context, group, children, groupItemTypeResourceMap, childrenResourceId);
        this.iPage = iPage;
        initPageConfig();
    }

    /**
     * 初始化分页参数
     */
    private void initPageConfig()
    {
        startIndex = getStartPageIndex() - 1;
        lastStartIndex = startIndex;
        pageSize = getPageSize();
        isLoading = false;
    }
    /**
     * 返回起始页下标, 默认为1
     * @return
     */
    public int getStartPageIndex()
    {
        return IPage2.DEFAULT_START_PAGE_INDEX;
    }

    /**
     * 返回分页大小, 默认为10
     * @return
     */
    public int getPageSize()
    {
        return IPage2.DEFAULT_PAGE_SIZE;
    }

    /**
     * 当前是否是第一页数据
     * @return
     */
    public boolean isFirstPage()
    {
        return startIndex <= getStartPageIndex();
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
            startIndex = getStartPageIndex();
        }
        else
        {
            startIndex += pageSize;
        }
        iPage.load(startIndex, startIndex + pageSize - 1);
    }

    /**
     * 起始下标递减
     */
    public void decreaseStartIndex()
    {
        startIndex--;
        checkBound();
    }

    /**
     * 起始下标递减
     */
    public void decreaseStartIndex(int size)
    {
        startIndex -= size;
        checkBound();
    }

    /**
     * 边界检测
     */
    private void checkBound()
    {
        if (startIndex < getStartPageIndex() - pageSize)
        {
            startIndex = getStartPageIndex() - pageSize;
        }
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
            lastStartIndex = startIndex;
        }
        else
        {
            startIndex = lastStartIndex;
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
    public void setDataSource(List<K> group, List<List<V>> children) {
        super.setDataSource(group, children);
        finishLoad(true);
    }

    /**
     * 增加数据
     * @param group
     * @param children
     */
    public final void addDataSource(List<K> group, List<List<V>> children)
    {
        if (mGroup == null)
        {
            mGroup = group;
        }
        else
        {
            if (startIndex == getStartPageIndex())
            {
                mGroup.clear();
            }
            mGroup.addAll(group);
        }

        if (mChildren == null)
        {
            mChildren = children;
        }
        else
        {
            if (startIndex == getStartPageIndex())
            {
                mChildren.clear();
            }
            mChildren.addAll(children);
        }

        finishLoad(true);
    }
}
