package com.jcgroup.common.framework.ui.adapter.recyclerview;

import android.view.View;

/**
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the RecyclerClickListener. This implements all methods in the
 * {@link RecyclerClickListener}
 */
public abstract class OnItemClickListener extends RecyclerClickListener {


    @Override
    public void onItemClick(View view, int position) {
        onCommonItemClick(view, position);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onItemChildClick(View view, int position) {

    }

    @Override
    public void onItemChildLongClick(View view, int position) {

    }

    public abstract void onCommonItemClick(View view, int position);
}
