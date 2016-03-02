package com.android.baseline.framework.ui.adapter;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * 多选适配器
 * 注意：选中与取消选中的泛型T必须是同一个对象或者重写equals方法
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/01/14 11:52]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public abstract class MultiChoiceAdapter<T> extends SingleChoiceAdapter<T> {
    public MultiChoiceAdapter(Context context, List<T> data, int resourceId) {
        super(context, data, resourceId);
    }

    public MultiChoiceAdapter(Context context, List<T> data, Map<Integer, Integer> itemTypeResourceMap) {
        super(context, data, itemTypeResourceMap);
    }

    /**
     * 选中某一项
     * @param choice
     */
    @Override
    public void selectItem(T choice)
    {
        if (choice != null && !selectedItems.contains(choice))
        {
            selectedItems.add(choice);
            notifyDataSetChanged();
        }
    }

    /**
     * 选中
     */
    public void selectAll(List<T> items)
    {
        selectedItems.removeAll(items);
        selectedItems.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 选中所有
     */
    public void selectAll()
    {
        selectedItems.clear();
        selectedItems.addAll(getDataSource());
        notifyDataSetChanged();
    }

    /**
     * 取消选中
     */
    public void disselectAll(List<T> items)
    {
        selectedItems.removeAll(items);
        notifyDataSetChanged();
    }

    /**
     * 取消选中所有
     */
    public void disselectAll()
    {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     * 返回所有选中的项目
     * @return
     */
    public List<T> getSelectedItems()
    {
        return selectedItems;
    }
}
