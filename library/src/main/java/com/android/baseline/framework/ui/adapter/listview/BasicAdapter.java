package com.android.baseline.framework.ui.adapter.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.baseline.framework.ui.adapter.IAdapter;
import com.android.baseline.framework.ui.adapter.MultiTypeSupport;
import com.android.baseline.framework.ui.adapter.ViewHolder;

import java.util.List;

/**
 * 基类Adapter
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-8-29]
 */
public abstract class BasicAdapter<T> extends BaseAdapter implements IAdapter<T> {
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData; // data source
    int mLayoutItemId = -1;
    MultiTypeSupport<T> multiTypeSupport;

    public BasicAdapter(Context context, List<T> data, int layoutItemId) {
        this(context, data, layoutItemId, null);
    }

    public BasicAdapter(Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport) {
        this(context, data, -1, multiTypeSupport);
    }

    public BasicAdapter(Context context, List<T> data, int layoutItemId, MultiTypeSupport<T> multiTypeSupport) {
        mContext = context;
        mData = data;
        mLayoutItemId = layoutItemId;
        this.multiTypeSupport = multiTypeSupport;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void setDataSource(List<T> data) {
        mData = data;
    }

    @Override
    public List<T> getDataSource() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int itemType = getItemViewType(position);
            @LayoutRes int layoutId = (mLayoutItemId != -1 && multiTypeSupport != null) ? multiTypeSupport.getLayoutId(itemType) : mLayoutItemId;
            convertView = mLayoutInflater.inflate(layoutId, null);
        }
        getView(position,
                convertView);
        return convertView;
    }

    /**
     * 子类需要实现
     *
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
    protected <V extends View> V findViewById(View convertView, int viewId) {
        if (convertView.getTag() == null) {
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        return viewHolder.findViewById(viewId);
    }

    @Override
    public int getItemViewType(int position) {
        if (mLayoutItemId == -1 && multiTypeSupport != null) {
            return multiTypeSupport.getItemViewType(getItem(position), position);
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        if (mLayoutItemId == -1 && multiTypeSupport != null) {
            return multiTypeSupport.getViewTypeCount();
        } else {
            return 1;
        }
    }
}
