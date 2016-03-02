package com.android.baseline.framework.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.baseline.framework.ui.util.ViewHolderUtil;

/**
 * 基类Adapter
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-8-29]
 */
public abstract class BasicAdapter<T> extends BaseAdapter
{
    protected Context mContext;
    private LayoutInflater mLayoutInflater;
    protected List<T> mData; // data source
    private Map<Integer, Integer> mItemTypeResourceMap = new HashMap<Integer, Integer>(); // 支持不同Item样式<样式类型, 资源文件id>
    private final int DEFAULT_ITEM_TYPE = 0; // 默认Item类型

    public BasicAdapter(Context context, List<T> data, int resourceId)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        mItemTypeResourceMap.put(DEFAULT_ITEM_TYPE, resourceId);
    }

    public BasicAdapter(Context context, List<T> data, Map<Integer, Integer> itemTypeResourceMap)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        mItemTypeResourceMap.putAll(itemTypeResourceMap);
    }
    
    public void setDataSource(List<T> data)
    {
        mData = data;
    }

    public List<T> getDataSource()
    {
        return mData;
    }

    @Override
    public int getCount()
    {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position)
    {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            int itemType = getItemViewType(position);
            convertView = mLayoutInflater.inflate(mItemTypeResourceMap.get(itemType),
                    null);
        }
        getView(position,
                convertView);
        return convertView;
    }

    /**
     * 子类需要实现, 使用ViewHolderUtil.findViewById{@link ViewHolderUtil}}, 无需实现Holder缓存机制
     * @param position
     * @param convertView
     */
    protected abstract void getView(final int position, final View convertView);
    
    /**
     * 类似convertView.findViewById(int viewId), 子类不需要关心如何使用ViewHolder机制
     * 
     * @param <V>
     * @param convertView
     * @param viewId
     * @return
     */
    protected <V extends View> V findViewById(View convertView, int viewId)
    {
        return ViewHolderUtil.get(convertView,
                viewId);
    }

    @Override
    public int getItemViewType(int position)
    {
        return DEFAULT_ITEM_TYPE;
    }

    @Override
    public int getViewTypeCount()
    {
        return mItemTypeResourceMap.size();
    }
}
