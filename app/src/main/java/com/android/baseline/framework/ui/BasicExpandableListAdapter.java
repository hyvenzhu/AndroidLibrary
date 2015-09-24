package com.android.baseline.framework.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.android.baseline.framework.ui.util.ViewHolderUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基类Adapter
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-12-16]
 */
public abstract class BasicExpandableListAdapter<K,V> extends BaseExpandableListAdapter
{
    protected Context mContext;
    private LayoutInflater mLayoutInflater;
    protected List<K> mGroup; // group data source
    protected List<List<V>> mChildren; // children data source

    private final int DEFAULT_ITEM_TYPE = 0; // 默认Item类型
    private Map<Integer, Integer> mGroupItemTypeResourceMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> mChildItemTypeResourceMap = new HashMap<Integer, Integer>();

    public BasicExpandableListAdapter(Context context, List<K> group, List<List<V>> children, int groupResourceId, int childrenResourceId)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mGroup = group;
        mChildren = children;
        mGroupItemTypeResourceMap.put(DEFAULT_ITEM_TYPE, groupResourceId);
        mChildItemTypeResourceMap.put(DEFAULT_ITEM_TYPE, childrenResourceId);
    }

    public BasicExpandableListAdapter(Context context, List<K> group, List<List<V>> children,
                Map<Integer, Integer> groupItemTypeResourceMap,
                Map<Integer, Integer> childItemTypeResourceMap)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mGroup = group;
        mChildren = children;
        mGroupItemTypeResourceMap.putAll(groupItemTypeResourceMap);
        mChildItemTypeResourceMap.putAll(childItemTypeResourceMap);
    }

    public BasicExpandableListAdapter(Context context, List<K> group, List<List<V>> children,
                  int groupResourceId,
                  Map<Integer, Integer> childItemTypeResourceMap)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mGroup = group;
        mChildren = children;
        mGroupItemTypeResourceMap.put(DEFAULT_ITEM_TYPE, groupResourceId);
        mChildItemTypeResourceMap.putAll(childItemTypeResourceMap);
    }

    public BasicExpandableListAdapter(Context context, List<K> group, List<List<V>> children,
                                      Map<Integer, Integer> groupItemTypeResourceMap,
                                      int childrenResourceId)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mGroup = group;
        mChildren = children;
        mGroupItemTypeResourceMap.putAll(groupItemTypeResourceMap);
        mChildItemTypeResourceMap.put(DEFAULT_ITEM_TYPE, childrenResourceId);
    }
    
    public void setDataSource(List<K> group, List<List<V>> children)
    {
        mGroup = group;
        mChildren = children;
    }
    
    public List<K> getGroupData()
    {
        return mGroup;
    }
    
    public List<List<V>> getChildrenData()
    {
        return mChildren;
    }

    public void addDataSource(List<K> group, List<List<V>> children)
    {
        if (mGroup == null)
        {
            mGroup = group;
        }
        else
        {
            mGroup.addAll(group);
        }
        if (mChildren == null)
        {
            mChildren = children;
        }
        else
        {
            mChildren.addAll(children);
        }
    }

    @Override
    public int getGroupCount()
    {
        return mGroup != null? mGroup.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return (mChildren != null && mChildren.size() > groupPosition)? mChildren.get(groupPosition).size() : 0;
    }

    @Override
    public K getGroup(int groupPosition)
    {
        return mGroup.get(groupPosition);
    }

    @Override
    public V getChild(int groupPosition, int childPosition)
    {
        return mChildren.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(mGroupItemTypeResourceMap.get(getGroupType(groupPosition)), null);
        }
        getGroupView(groupPosition, isExpanded, convertView);
        return convertView;
    }
    

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(mChildItemTypeResourceMap.get(getChildType(groupPosition, childPosition)), null);
        }
        getChildView(groupPosition, childPosition, isLastChild, convertView);
        return convertView;
    }
    
    /**
     * 子类需要实现, 使用ViewHolderUtil.findViewById{@link ViewHolderUtil}}, 无需实现Holder缓存机制
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     */
    protected abstract void getGroupView(int groupPosition, boolean isExpanded, View convertView);
    
    /**
     * 子类需要实现, 使用ViewHolderUtil.findViewById{@link ViewHolderUtil}}, 无需实现Holder缓存机制
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     */
    protected abstract void getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
            View convertView);
    
    /**
     * 类似convertView.findViewById(int viewId), 子类不需要关心如何使用ViewHolder机制
     * 
     * @param convertView
     * @param viewId
     * @return
     */
    protected <Vi extends View> Vi findViewById(View convertView, int viewId)
    {
        return ViewHolderUtil.get(convertView,
                viewId);
    }

    @Override
    public int getGroupTypeCount()
    {
        return mGroupItemTypeResourceMap.size();
    }

    @Override
    public int getGroupType(int groupPosition)
    {
        return DEFAULT_ITEM_TYPE;
    }

    @Override
    public int getChildTypeCount()
    {
        return mChildItemTypeResourceMap.size();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition)
    {
        return DEFAULT_ITEM_TYPE;
    }
}
