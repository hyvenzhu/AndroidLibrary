package com.android.baseline.framework.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * 通用ViewHolder
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 13:47]
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    SparseArray<View> mViews;
    View mItemView;

    public ViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<View>();
    }

    public <T extends View> T findViewById(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mItemView.findViewById(id);
            mViews.put(id,
                    view);
        }
        return (T) view;
    }
}
