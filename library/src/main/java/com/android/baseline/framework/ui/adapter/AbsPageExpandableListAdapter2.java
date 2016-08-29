package com.android.baseline.framework.ui.adapter;

import android.content.Context;

import com.android.baseline.framework.logic.page.IPage;
import com.android.baseline.framework.logic.page.Page2;
import com.android.baseline.framework.ui.BasicExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * 分页适配器(start、end模式)
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-09-29 21:54]
 */
public abstract class AbsPageExpandableListAdapter2<K, V> extends BasicExpandableListAdapter<K, V> {
    Page2 page2;

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children, int groupResourceId, int childrenResourceId, Page2 page2) {
        super(context, group, children, groupResourceId, childrenResourceId);
        initPage(page2);
    }

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children,
                                        Map<Integer, Integer> groupItemTypeResourceMap,
                                        Map<Integer, Integer> childItemTypeResourceMap, Page2 page2) {
        super(context, group, children, groupItemTypeResourceMap, childItemTypeResourceMap);
        initPage(page2);
    }

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children,
                                        int groupResourceId,
                                        Map<Integer, Integer> childItemTypeResourceMap, Page2 page2) {
        super(context, group, children, groupResourceId, childItemTypeResourceMap);
        initPage(page2);
    }

    public AbsPageExpandableListAdapter2(Context context, List<K> group, List<List<V>> children,
                                        Map<Integer, Integer> groupItemTypeResourceMap,
                                        int childrenResourceId, Page2 page2) {
        super(context, group, children, groupItemTypeResourceMap, childrenResourceId);
        initPage(page2);
    }

    /**
     * 初始化分页参数
     */
    private void initPage(Page2 page2)
    {
        this.page2 = page2;
        page2.setStartPageIndex(getStartPageIndex())
                .setPageSize(getPageSize());
    }
    /**
     * 返回起始页下标, 默认为1
     * @return
     */
    public int getStartPageIndex()
    {
        return IPage.DEFAULT_START_PAGE_INDEX;
    }

    /**
     * 返回分页大小, 默认为10
     * @return
     */
    public int getPageSize()
    {
        return IPage.DEFAULT_PAGE_SIZE;
    }

    /**
     * 当前是否是第一页数据
     * @return
     */
    public boolean isFirstPage()
    {
        return page2.isFirstPage();
    }

    /**
     * 分页加载数据
     * [可能会抛出异常，请确认数据加载结束后，你已经调用了finishLoad(boolean success)方法]
     * @param isFirstPage true: 第一页  false: 下一页
     */
    public void loadPage(boolean isFirstPage)
    {
        page2.loadPage(isFirstPage);
    }

    /**
     * 起始下标递减
     */
    public void decreaseStartIndex()
    {
        page2.decreaseStartIndex();
    }

    /**
     * 起始下标递减
     */
    public void decreaseStartIndex(int size)
    {
        page2.decreaseStartIndex(size);
    }

    /**
     * 加载结束
     * @param success true：加载成功  false：失败(无数据)
     */
    public void finishLoad(boolean success)
    {
        page2.finishLoad(success);
    }

    @Override
    public void setDataSource(List<K> group, List<List<V>> children) {
        if ((group == null || group.size() == 0)
                && (children == null || children.size() == 0))
        {
            finishLoad(false);
        }
        else
        {
            super.setDataSource(group, children);
            finishLoad(true);
        }
    }

    /**
     * 增加数据
     * @param group
     * @param children
     */
    public final void addDataSource(List<K> group, List<List<V>> children)
    {
        if ((group == null || group.size() == 0)
                && (children == null || children.size() == 0))
        {
            if (isFirstPage()) // 第一页,清空数据
            {
                if (mGroup != null)
                {
                    mGroup.clear();
                }
                if (mChildren != null)
                {
                    mChildren.clear();
                }
            }

            finishLoad(false);
        }
        else
        {
            if (mGroup == null)
            {
                mGroup = group;
            }
            else
            {
                if (isFirstPage())
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
                if (isFirstPage())
                {
                    mChildren.clear();
                }
                mChildren.addAll(children);
            }

            finishLoad(true);
        }
    }
}
