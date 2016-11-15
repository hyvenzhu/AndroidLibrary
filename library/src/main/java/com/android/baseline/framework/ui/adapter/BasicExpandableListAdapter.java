package com.android.baseline.framework.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * 基类Adapter
 * P: 父
 * C: 子
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-12-16]
 */
public abstract class BasicExpandableListAdapter<P, C> extends BaseExpandableListAdapter implements IAdapter {
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<P> mData;
    int groupResourceId;
    int childrenResourceId;

    public BasicExpandableListAdapter(Context context, List<P> data, int groupResourceId, int childrenResourceId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        this.groupResourceId = groupResourceId;
        this.childrenResourceId = childrenResourceId;
    }

    @Override
    public void setDataSource(List data) {
        mData = data;
    }

    @Override
    public List<P> getDataSource() {
        return mData;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public P getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * 返回某一个分组内的Children集合
     *
     * @param groupPosition
     * @return
     */
    public abstract List<C> getChilrenList(int groupPosition);

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return getChilrenList(groupPosition).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getChilrenList(groupPosition) != null ? getChilrenList(groupPosition).size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(groupResourceId, null);
        }
        getGroupView(groupPosition, isExpanded, convertView);
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(childrenResourceId, null);
        }
        getChildView(groupPosition, childPosition, isLastChild, convertView);
        return convertView;
    }

    /**
     * 子类需要实现
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     */
    protected abstract void getGroupView(int groupPosition, boolean isExpanded, View convertView);

    /**
     * 子类需要实现
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     */
    protected abstract void getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                         View convertView);

    ViewHolder viewHolder;

    /**
     * 类似convertView.findViewById(int viewId), 子类不需要关心如何使用ViewHolder机制
     *
     * @param convertView
     * @param viewId
     * @return
     */
    protected <Vi extends View> Vi findViewById(View convertView, int viewId) {
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        return viewHolder.findViewById(viewId);
    }
}
