package com.android.baseline.framework.ui.adapter.page;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import com.android.baseline.framework.ui.adapter.recyclerview.IAdapter;

import java.util.List;

/**
 * 分页
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 15:29]
 */
public class PageWrapper<T> {
    IPage page;
    IAdapter<T> iAdapter;

    public PageWrapper(IAdapter<T> iAdapter, IPage page) {
        this.iAdapter = iAdapter;
        initPage(page);
    }

    /**
     * 初始化分页参数
     */
    private void initPage(IPage page) {
        this.page = page;
        this.page.setStartPageIndex(IPage.DEFAULT_START_PAGE_INDEX)
                .setPageSize(IPage.DEFAULT_PAGE_SIZE);
    }

    /**
     * 当前是否是第一页数据
     *
     * @return
     */
    public boolean isFirstPage() {
        return page.isFirstPage();
    }

    /**
     * 分页加载数据
     * [可能会抛出异常，请确认数据加载结束后，你已经调用了finishLoad(boolean success)方法]
     *
     * @param isFirstPage true: 第一页  false: 下一页
     */
    public void loadPage(boolean isFirstPage) {
        page.loadPage(isFirstPage);
    }

    /**
     * 加载结束
     *
     * @param success true：加载成功  false：失败(无数据)
     */
    public void finishLoad(boolean success) {
        page.finishLoad(success);
        notifyDataSetChanged();
    }

    /**
     * 增加数据
     *
     * @param data
     */
    public final void addDataSource(List<T> data) {
        List<T> mData = iAdapter.getDataSource();
        if (isFirstPage()) // 当前是第一页, 则重置数据源
        {
            iAdapter.setDataSource(data);
        } else if (data != null) {
            mData.addAll(data);
        }
        finishLoad(data != null && data.size() > 0);
    }

    /**
     * 起始下标递减
     */
    public void decreaseStartIndex() {
        page.decreaseStartIndex();
    }

    /**
     * 起始下标递减
     */
    public void decreaseStartIndex(int size) {
        page.decreaseStartIndex(size);
    }

    /**
     * All数据源
     *
     * @return
     */
    public List<T> getDataSource() {
        return iAdapter.getDataSource();
    }

    /**
     * 指定position的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return iAdapter.getItem(position);
    }

    /**
     * adapter刷新
     */
    public void notifyDataSetChanged() {
        if (iAdapter instanceof BaseAdapter) {
            ((BaseAdapter) iAdapter).notifyDataSetChanged();
        } else if (iAdapter instanceof BaseExpandableListAdapter) {
            ((BaseExpandableListAdapter) iAdapter).notifyDataSetChanged();
        } else if (iAdapter instanceof RecyclerView.Adapter) {
            ((RecyclerView.Adapter) iAdapter).notifyDataSetChanged();
        }
    }

    public boolean isLoading() {
        return page.isLoading;
    }
}
