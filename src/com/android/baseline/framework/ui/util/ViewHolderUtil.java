package com.android.baseline.framework.ui.util;

import android.util.SparseArray;
import android.view.View;
/**
 * ViewHolder工具类
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-8-29]
 */
public class ViewHolderUtil
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
    
    /**
     * 类似convertView.findViewById(int viewId), 子类不需要关心如何使用ViewHolder机制
     * @param <V>
     * @param convertView
     * @param viewId
     * @return
     */
    protected static <V extends View> V findViewById(View convertView, int viewId)
    {
        return ViewHolderUtil.get(convertView,
                viewId);
    }
}
