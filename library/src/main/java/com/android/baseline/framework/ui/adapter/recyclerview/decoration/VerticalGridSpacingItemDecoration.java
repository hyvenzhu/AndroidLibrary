package com.android.baseline.framework.ui.adapter.recyclerview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 垂直排列的 GridLayoutManager 间距
 * @author hiphonezhu@gmail.com
 * @version [jcease-android, 2017/8/2]
 */
public class VerticalGridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    
    private int spacing;
    private boolean includeEdge;
    
    public VerticalGridSpacingItemDecoration(int spacing, boolean includeEdge) {
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount = gridLayoutManager.getSpanCount();
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
            
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                
                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / 2; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / 2; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
