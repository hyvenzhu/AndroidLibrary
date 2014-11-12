package com.android.baseline.framework.ui;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 基类Adapter
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-8-29]
 */
public abstract class BasicAdapter<T> extends BaseAdapter
{
    private LayoutInflater mLayoutInflater;
    protected List<T> mData;
    private int mResourceId;

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

    /**
     * 类似getView(int position, View convertView, ViewGroup parent),
     * 子类不需要关心View的复用机制
     * 
     * @param position
     * @param convertView
     */
    protected abstract void getView(final int position, final View convertView);

    /**
     * ViewHolder工具类
     * 
     * @author hiphonezhu@gmail.com
     * @version [Android-BaseLine, 2014-8-29]
     */
    private static class ViewHolderUtil
    {
        private ViewHolderUtil()
        {
        }

        /**
         * 
         * @param <T> View子类
         * @param view convertView
         * @param id 需要查找的view
         * @return
         */
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View convertView, int id)
        {
            SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
            if (viewHolder == null)
            {
                viewHolder = new SparseArray<View>();
                convertView.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null)
            {
                childView = convertView.findViewById(id);
                viewHolder.put(id,
                        childView);
            }
            return (T) childView;
        }
    }
}
