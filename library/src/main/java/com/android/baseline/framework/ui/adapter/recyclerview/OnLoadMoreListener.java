package com.android.baseline.framework.ui.adapter.recyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView 加载更多
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/6/23 15:11]
 */
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (!ViewCompat.canScrollVertically(recyclerView, 1)) { // 滑动到最底部了
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
