package com.android.baseline.framework.ui;

import java.util.List;

import com.android.baseline.framework.ui.util.ViewHolderUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 基类Adapter
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-8-29]
 */
public abstract class BasicAdapter<T> extends BaseAdapter
{
    private LayoutInflater mLayoutInflater;
    protected List<T> mData; // data source
    private int mResourceId; // layout id

    public BasicAdapter(Context context, List<T> data, int resourceId)
    {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        mResourceId = resourceId;
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
            convertView = mLayoutInflater.inflate(mResourceId,
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
}
