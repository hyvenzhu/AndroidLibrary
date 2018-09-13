package library.common.framework.ui.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * 通用ViewHolder
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    SparseArray<View> mViews;
    public View mItemView;
    
    private final LinkedHashSet<Integer> childClickViewIds;
    
    private final LinkedHashSet<Integer> itemChildLongClickViewIds;
    
    public ViewHolder(View itemView) {
        super(itemView);
        this.childClickViewIds = new LinkedHashSet<>();
        this.itemChildLongClickViewIds = new LinkedHashSet<>();
        this.mItemView = itemView;
        this.mViews = new SparseArray<>();
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
    
    public HashSet<Integer> getItemChildLongClickViewIds() {
        return itemChildLongClickViewIds;
    }
    
    public HashSet<Integer> getChildClickViewIds() {
        return childClickViewIds;
    }
    
    /**
     * add childView id
     *
     * @param viewId add the child view id can support childView click
     * @return
     */
    public ViewHolder addOnClickListener(int viewId) {
        childClickViewIds.add(viewId);
        return this;
    }
    
    /**
     * add long click view id
     *
     * @param viewId
     * @return
     */
    public ViewHolder addOnLongClickListener(int viewId) {
        itemChildLongClickViewIds.add(viewId);
        return this;
    }
}
