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
        if (parentView.getChildAt(viewIndex) != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            parentView.removeViewAt(viewIndex);
            if (view != mView) {
                if (mView.getParent() != null) {
                    ((ViewGroup) mView.getParent()).removeView(mView);
                }
            }
            parentView.addView(view, viewIndex, params);
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
