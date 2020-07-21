package library.common.framework.ui.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * 多样式支持适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class MultiTypeAdapter<T> extends CommonAdapter<T> {
    MultiTypeSupport<T> mItemSupport;

    public MultiTypeAdapter(Context context, MultiTypeSupport<T> itemSupport) {
        super(context, -1);
        mItemSupport = itemSupport;
    }

    public MultiTypeAdapter(Context context, List<T> data, MultiTypeSupport<T> itemSupport) {
        super(context, data, -1);
        mItemSupport = itemSupport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder commonViewHolder = new ViewHolder(
                LayoutInflater.from(mContext).inflate(mItemSupport.getLayoutId(viewType), parent, false));
        return commonViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemSupport.getItemViewType(getItem(position), position);
    }
}
