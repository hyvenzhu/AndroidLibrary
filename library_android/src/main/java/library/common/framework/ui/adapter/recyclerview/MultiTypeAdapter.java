package library.common.framework.ui.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;
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
        View itemView = LayoutInflater.from(mContext).inflate(mItemSupport.getLayoutId(viewType), parent, false);

        Class viewBindingClass;
        Object viewBinding = null;
        if ((viewBindingClass = getViewBindClass()) != null) {
            try {
                Method bindMethod = viewBindingClass.getMethod("bind", new Class[]{View.class});
                viewBinding = bindMethod.invoke(null, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ViewHolder commonViewHolder = new ViewHolder(itemView, viewBinding);
        return commonViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemSupport.getItemViewType(getItem(position), position);
    }
}
