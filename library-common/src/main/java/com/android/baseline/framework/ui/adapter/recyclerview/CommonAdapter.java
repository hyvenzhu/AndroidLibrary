package com.android.baseline.framework.ui.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * 通用RecyclerView适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/19 13:42]
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements IAdapter<T> {
    protected Context mContext;
    int mItemLayoutId;
    protected List<T> mData;

    public CommonAdapter(Context context, List<T> data, int itemLayoutId) {
        mContext = context;
        mData = data;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder commonViewHolder = new ViewHolder(
                LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false));
        return commonViewHolder;
    }

    @Override
    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position > getItemCount() - 1) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public void setDataSource(List<T> data) {
        mData = data;
    }

    @Override
    public List<T> getDataSource() {
        return mData;
    }
    
    public void setBackgroundResource(ViewHolder holder, @IdRes int viewId, @DrawableRes int resid) {
        holder.findViewById(viewId).setBackgroundResource(resid);
    }
    
    public void setBackgroundColor(ViewHolder holder, @IdRes int viewId, @ColorRes int color) {
        holder.findViewById(viewId).setBackgroundColor(ContextCompat.getColor(mContext, color));
    }
    
    public void setText(ViewHolder holder, @IdRes int viewId, String text) {
        TextView tv = holder.findViewById(viewId);
        tv.setText(text);
    }
    
    public void setText(ViewHolder holder, @IdRes int viewId, @StringRes int text) {
        TextView tv = holder.findViewById(viewId);
        tv.setText(text);
    }
    
    public void setTextColor(ViewHolder holder, @IdRes int viewId, @ColorRes int color) {
        TextView tv = holder.findViewById(viewId);
        tv.setTextColor(ContextCompat.getColor(mContext, color));
    }
    
    public void setImageUrl(ViewHolder holder, @IdRes int viewId, String url) {
        ImageView iv = holder.findViewById(viewId);
        // TODO: 2017/12/21
    }
    
    public void setImageUrl(ViewHolder holder, @IdRes int viewId, String url, @DrawableRes int placeHolder, @DrawableRes int errorHolder) {
        ImageView iv = holder.findViewById(viewId);
        // TODO: 2017/12/21
    }
    
    public void setCircleImageUrl(ViewHolder holder, @IdRes int viewId, String url) {
        ImageView iv = holder.findViewById(viewId);
        // TODO: 2017/12/21
    }
    
    public void setRoundImageUrl(ViewHolder holder, @IdRes int viewId, String url, @DrawableRes int placeHolder, @DrawableRes int errorHolder, int cornerRadius) {
        ImageView iv = holder.findViewById(viewId);
        // TODO: 2017/12/21
    }
    
    public void setProgress(ViewHolder holder, @IdRes int viewId, int maxPorgress, int currentPorgress) {
        ProgressBar pb = holder.findViewById(viewId);
        pb.setMax(maxPorgress);
        pb.setProgress(currentPorgress);
    }
    
    public void setVisibility(ViewHolder holder, @IdRes int viewId, int visibility) {
        holder.findViewById(viewId).setVisibility(visibility);
    }
}
