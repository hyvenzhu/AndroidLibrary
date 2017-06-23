package com.android.baseline.framework.ui.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.baseline.framework.ui.adapter.MultiTypeSupport;
import com.android.baseline.framework.ui.adapter.ViewHolder;

import java.util.List;

/**
 * 多样式支持适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 14:16]
 */
public class MultiTypeAdapter<T> extends CommonAdapter<T> {
    MultiTypeSupport<T> mItemSupport;

    protected MultiTypeAdapter(Context context, List<T> data, MultiTypeSupport<T> itemSupport) {
        super(context, data, -1);
        mItemSupport = itemSupport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder commonViewHolder = new ViewHolder(
                LayoutInflater.from(mContext).inflate(mItemSupport.getLayoutId(viewType), parent, false));
        return commonViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemSupport.getItemViewType(getItem(position), position);
    }
}
