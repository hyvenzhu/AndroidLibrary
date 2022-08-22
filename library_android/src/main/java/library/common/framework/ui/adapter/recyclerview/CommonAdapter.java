package library.common.framework.ui.adapter.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import library.common.util.NoDoubleClickListener;

/**
 * 通用RecyclerView适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements IAdapter<T> {
    protected Context mContext;
    int mItemLayoutId;
    protected List<T> mData;
    private ViewHolder viewHolder;

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
        View itemView = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);

        Class viewBindingClass;
        Object viewBinding = null;
        if ((viewBindingClass = getViewBindClass(viewType)) != null) {
            try {
                Method bindMethod = viewBindingClass.getMethod("bind", new Class[]{View.class});
                viewBinding = bindMethod.invoke(null, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        viewHolder = new ViewHolder(itemView, viewBinding);
        return viewHolder;
    }

    protected Class getViewBindClass(int viewType) {
        return null;
    }

    protected View getItemView() {
        return viewHolder.mItemView;
    }

    @Override
    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public final int getRealItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position > getItemCount() - 1) {
            return null;
        }
        return mData.get(position);
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    public void insertData(int position, T appendedData) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(position, appendedData);
    }

    public void appendData(T appendedData) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(appendedData);
    }

    @Override
    public void setDataSource(List<T> data) {
        mData = data;
    }

    public void appendData(List<T> appendedData) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(appendedData);
    }

    public void remove(int position) {
        if (getItemCount() > position) {
            getDataSource().remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(T item) {
        getDataSource().remove(item);
        notifyDataSetChanged();
    }

    @Override
    public List<T> getDataSource() {
        return mData;
    }

    public T getLastItem() {
        if (getItemCount() > 0) {
            return getItem(getItemCount() - 1);
        }
        return null;
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

    public void setImageSrc(ViewHolder holder, @IdRes int viewId, @DrawableRes int src) {
        ImageView iv = holder.findViewById(viewId);
        iv.setImageResource(src);
    }

    public void setImageBitmap(ViewHolder holder, @IdRes int viewId, Bitmap bitmap) {
        ImageView iv = holder.findViewById(viewId);
        iv.setImageBitmap(bitmap);
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

    public void setOnClickListener(ViewHolder holder, @IdRes int viewId, final View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            holder.findViewById(viewId).setOnClickListener(null);
        } else {
            holder.findViewById(viewId).setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(ViewHolder holder, final View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            holder.mItemView.setOnClickListener(null);
        } else {
            holder.mItemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                }
            });
        }
    }

    public void setOnItemLongClickListener(ViewHolder holder, View.OnLongClickListener onLongClickListener) {
        holder.mItemView.setOnLongClickListener(onLongClickListener);
    }
}
