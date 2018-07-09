package com.jcgroup.common.framework.ui.adapter.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.jcgroup.common.framework.image.ImageLoaderFactory;

import java.util.Collections;
import java.util.List;

/**
 * 通用RecyclerView适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements IAdapter<T> {
    protected Context mContext;
    int mItemLayoutId;
    protected List<T> mData;
    
    public CommonAdapter(Context context, int itemLayoutId) {
        this(context, null, itemLayoutId);
    }
    
    public CommonAdapter(Context context, List<T> data, int itemLayoutId) {
        mContext = context;
        mData = data;
        mItemLayoutId = itemLayoutId;
        setHasStableIds(true);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
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
    
    public void appendData(List<T> appendedData) {
        if (mData == null) {
            mData = Collections.emptyList();
        }
        mData.addAll(appendedData);
    }
    
    public void remove(int position) {
        if (getItemCount() > position) {
            getDataSource().remove(position);
        }
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
        ImageLoaderFactory.createDefault().display(mContext, iv, url);
    }
    
    public void setImageSrc(ViewHolder holder, @IdRes int viewId, @DrawableRes int src) {
        ImageView iv = holder.findViewById(viewId);
        iv.setImageResource(src);
    }
    
    public void setImageBitmap(ViewHolder holder, @IdRes int viewId, Bitmap bitmap) {
        ImageView iv = holder.findViewById(viewId);
        iv.setImageBitmap(bitmap);
    }

    public void setImageUrl(ViewHolder holder, @IdRes int viewId, String url, @DrawableRes int placeHolder) {
        ImageView iv = holder.findViewById(viewId);
        ImageLoaderFactory.createDefault().display(mContext, iv, url, placeHolder, placeHolder);
    }
    
    public void setImageUrl(ViewHolder holder, @IdRes int viewId, String url, @DrawableRes int placeHolder, @DrawableRes int errorHolder) {
        ImageView iv = holder.findViewById(viewId);
        ImageLoaderFactory.createDefault().display(mContext, iv, url, placeHolder, errorHolder);
    }
    
    public void setCircleImageUrl(ViewHolder holder, @IdRes int viewId, String url) {
        ImageView iv = holder.findViewById(viewId);
        ImageLoaderFactory.createDefault().displayCircle(mContext, iv, url);
    }
    
    public void setCircleImageUrl(ViewHolder holder, @IdRes int viewId, String url, @DrawableRes int placeHolder, @DrawableRes int errorHolder) {
        ImageView iv = holder.findViewById(viewId);
        ImageLoaderFactory.createDefault().displayCircle(mContext, iv, url, placeHolder, errorHolder);
    }
    
    public void setRoundImageUrl(ViewHolder holder, @IdRes int viewId, String url, int cornerRadius) {
        ImageView iv = holder.findViewById(viewId);
        ImageLoaderFactory.createDefault().displayRoundedCorners(mContext, iv, url, cornerRadius);
    }
    
    public void setRoundImageUrl(ViewHolder holder, @IdRes int viewId, String url, int cornerRadius, @DrawableRes int placeHolder, @DrawableRes int errorHolder) {
        ImageView iv = holder.findViewById(viewId);
        ImageLoaderFactory.createDefault().displayRoundedCorners(mContext, iv, url, cornerRadius, placeHolder, errorHolder);
    }
    
    public void setProgress(ViewHolder holder, @IdRes int viewId, int maxPorgress, int currentProgress) {
        ProgressBar pb = holder.findViewById(viewId);
        pb.setMax(maxPorgress);
        pb.setProgress(currentProgress);
    }
    
    public void setVisibility(ViewHolder holder, @IdRes int viewId, int visibility) {
        holder.findViewById(viewId).setVisibility(visibility);
    }
    
    public void setItemVisibility(ViewHolder holder, int visibility) {
        holder.mItemView.setVisibility(visibility);
    }
    
    public void setOnClickListener(ViewHolder holder, @IdRes int viewId, View.OnClickListener onClickListener) {
        holder.findViewById(viewId).setOnClickListener(onClickListener);
    }
    
    public void setOnItemClickListener(ViewHolder holder, View.OnClickListener onClickListener) {
        holder.mItemView.setOnClickListener(onClickListener);
    }
    
    public void setOnItemLongClickListener(ViewHolder holder, View.OnLongClickListener onLongClickListener) {
        holder.mItemView.setOnLongClickListener(onLongClickListener);
    }
}
