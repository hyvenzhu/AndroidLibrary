package com.android.baseline.framework.ui;

import android.widget.ExpandableListView;

/**
 * 分组ListView展开某一个自动关闭其他
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-12-16]
 */
public class OnGroupStateChangeListener implements ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener {
    ExpandableListView expandableListView;
    ExpandableListView.OnGroupExpandListener onGroupExpandListener;
    ExpandableListView.OnGroupCollapseListener onGroupCollapseListener;
    int lastExpandedGroup = -1;

    public OnGroupStateChangeListener(ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
    }

    /**
     * 设置展开回调
     * @param onGroupExpandListener
     */
    public OnGroupStateChangeListener setOnGroupExpandListener(ExpandableListView.OnGroupExpandListener onGroupExpandListener)
    {
        this.onGroupExpandListener = onGroupExpandListener;
        return this;
    }

    /**
     * 设置合并回调
     * @param onGroupCollapseListener
     */
    public OnGroupStateChangeListener setOnGroupCollapseListener(ExpandableListView.OnGroupCollapseListener onGroupCollapseListener)
    {
        this.onGroupCollapseListener = onGroupCollapseListener;
        return this;
    }

    /**
     * 绑定事件
     */
    public void bind()
    {
        expandableListView.setOnGroupExpandListener(this);
        expandableListView.setOnGroupCollapseListener(this);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (lastExpandedGroup != groupPosition) {
            // 关闭上一个
            expandableListView.collapseGroup(lastExpandedGroup);
        }
        lastExpandedGroup = groupPosition;
        if (onGroupExpandListener != null)
        {
            onGroupExpandListener.onGroupExpand(groupPosition);
        }
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        if (lastExpandedGroup == groupPosition) {
            lastExpandedGroup = -1;
        }
        if (onGroupCollapseListener != null)
        {
            onGroupCollapseListener.onGroupCollapse(groupPosition);
        }
    }
}
