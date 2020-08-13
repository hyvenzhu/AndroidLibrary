package library.common.framework.ui.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 两个 View 切换显示
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-12]
 */
public class ViewSwitcher {
    /**
     * 原内容View参数
     */
    private View mView;
    private ViewGroup parentView;
    private int viewIndex;
    private ViewGroup.LayoutParams params;
    int height;

    /**
     * 加载布局
     */
    View loadingView;

    /**
     * @param view 初始化要显示的 View
     */
    public ViewSwitcher(View view) {
        super();
        mView = view;
        init();
    }

    /**
     * 显示特定的 View，替换 mView
     *
     * @param view
     */
    public void show(View view) {
        loadingView = view;
        if (parentView == null) {
            init();
        }
        if (view != mView) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            // 显示其他视图
            mView.setVisibility(View.GONE);
            if (parentView.getChildAt(viewIndex) != mView) {
                parentView.removeViewAt(viewIndex);
            }
            parentView.addView(view, viewIndex, params);
        } else {
            // 显示原视图
            mView.setVisibility(View.VISIBLE);
            if (parentView.getChildAt(viewIndex) != mView) {
                parentView.removeViewAt(viewIndex);
            }
        }
    }

    public void show(int layoutId) {
        show(inflate(layoutId));
    }

    /**
     * 显示 mView
     */
    public void reset() {
        if (loadingView != mView) {
            show(mView);
        }
    }

    public View inflate(int layoutId) {
        return LayoutInflater.from(mView.getContext()).inflate(layoutId, null);
    }

    /**
     * 查找 mView 的 Index
     */
    private void init() {
        params = mView.getLayoutParams();
        height = params.height;
        if (mView.getParent() != null) {
            parentView = (ViewGroup) mView.getParent();
        } else {
            parentView = mView.getRootView().findViewById(
                    android.R.id.content);
        }
        int count = parentView.getChildCount();
        for (int index = 0; index < count; index++) {
            if (mView == parentView.getChildAt(index)) {
                viewIndex = index;
                break;
            }
        }
    }
}
