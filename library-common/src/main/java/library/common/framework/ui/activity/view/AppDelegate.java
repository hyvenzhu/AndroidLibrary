package library.common.framework.ui.activity.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import library.common.R;
import library.common.framework.ui.widget.AbstractLoadHelper;
import library.common.util.Callback;
import library.common.util.NoDoubleClickListener;
import library.common.util.StatusBarUtils;

/**
 * View delegate base class
 * 视图层代理的基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class AppDelegate implements IDelegate {
    protected final SparseArray<View> mViews = new SparseArray<View>();

    private ViewGroup rootView;
    private ViewGroup titleGroup;
    private Context context;
    private Fragment fragment;
    private Callback callback;
    private boolean isVisible;
    protected boolean isActivity;
    private List<ViewController> viewControllers = new ArrayList<>();

    public <T> void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public <T> void doCall(T data) {
        if (callback != null) {
            callback.call(data);
        }
    }

    public void doCall() {
        doCall(null);
    }

    /**
     * 返回标题栏，通过重写自定义标题栏
     *
     * @param root
     * @return
     */
    protected abstract View getTitleView(ViewGroup root);

    /**
     * 返回内容布局
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    @Override
    public void create(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = context;
        isActivity = true;
        rootView = (ViewGroup) getRootView();
        if (rootView == null) {
            rootView = (ViewGroup) inflater.inflate(R.layout.com_activity_common, container, false);
        }
        titleGroup = rootView.findViewWithTag("title");
        ViewGroup content = rootView.findViewWithTag("content");

        View titleView = getTitleView(titleGroup);
        if (titleView != null) {
            titleGroup.setVisibility(View.VISIBLE);
        } else {
            titleGroup.setVisibility(View.GONE);
        }

        // 内容布局
        int contentLayoutId = getContentLayoutId();
        if (contentLayoutId > 0) {
            inflater.inflate(contentLayoutId, content, true);

            // ButterKnife
            ButterKnife.bind(this, rootView);
            // 初始化加载布局
            initLoadViewHelper(content);
        }
    }

    @Override
    public void create(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragment = fragment;
        create(fragment.getContext(), inflater, container, savedInstanceState);
        isActivity = false;
    }

    public void hideTitle() {
        titleGroup.setVisibility(View.GONE);
    }

    public void showTitle() {
        titleGroup.setVisibility(View.VISIBLE);
    }

    /**
     * 自定义标题栏适应状态栏（标题栏高度需要为wrap_content或者match_parent）
     *
     * @param title
     */
    public void fitCustomTitle(View title) {
        StatusBarUtils.setTranslucent(getActivity(), null);
        if (title != null) {
            Object tag = title.getTag(R.id.com_paddingTop);
            if (tag == null) {
                tag = title.getPaddingTop();
                title.setTag(R.id.com_paddingTop, tag);
            }
            int paddingTop = Integer.parseInt(tag.toString());
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                title.setPadding(title.getPaddingLeft(), paddingTop + statusBarHeight,
                        title.getPaddingRight(), title.getPaddingBottom());
            }
        }
    }

    public void cancelFitCustomTitle(View title) {
        StatusBarUtils.setTranslucent(getActivity(), null);
        if (title != null) {
            Object tag = title.getTag(R.id.com_paddingTop);
            if (tag == null) {
                tag = title.getPaddingTop();
                title.setTag(R.id.com_paddingTop, tag);
            }
            int paddingTop = Integer.parseInt(tag.toString());
            title.setPadding(title.getPaddingLeft(), paddingTop,
                    title.getPaddingRight(), title.getPaddingBottom());
        }
    }

    public void fitCustomTitle(Fragment fragment, View title) {
        StatusBarUtils.setTranslucent(fragment, null);
        if (title != null) {
            Object tag = title.getTag(R.id.com_paddingTop);
            if (tag == null) {
                tag = title.getPaddingTop();
                title.setTag(R.id.com_paddingTop, tag);
            }
            int paddingTop = Integer.parseInt(tag.toString());
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                title.setPadding(title.getPaddingLeft(), paddingTop + statusBarHeight,
                        title.getPaddingRight(), title.getPaddingBottom());
            }
        }
    }

    /**
     * 设置通用标题栏颜色（状态栏会随之改变）
     *
     * @param color
     */
    public void setCommonTitleColor(@ColorInt int color) {
        StatusBarUtils.setTranslucent(getActivity(), null);
        titleGroup.setBackgroundColor(color);
    }

    /**
     * 设置状态栏字体黑色（需要先调用{@link AppDelegate#fitCustomTitle(View)}、{@link AppDelegate#fitCustomTitle(Fragment, View)}）
     *
     * @param activity
     */
    public void setLightMode(Activity activity) {
        StatusBarUtils.setLightMode(activity);
    }

    /**
     * 设置状态栏字体白色（需要先调用{@link AppDelegate#fitCustomTitle(View)}、{@link AppDelegate#fitCustomTitle(Fragment, View)}）
     *
     * @param activity
     */
    public void setDarkMode(Activity activity) {
        StatusBarUtils.setDarkMode(activity);
    }

    /**
     * 标题栏隐藏和切换
     *
     * @param visible
     */
    public void setTitleVisible(boolean visible) {
        if (visible) {
            titleGroup.setVisibility(View.VISIBLE);
        } else {
            titleGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShow() {
        isVisible = true;
        for (ViewController controller : viewControllers) {
            controller.onShow();
        }
    }

    @Override
    public void onHide() {
        isVisible = false;
        for (ViewController controller : viewControllers) {
            controller.onHide();
        }
    }

    @Override
    public void onDestroy() {
        for (ViewController controller : viewControllers) {
            controller.onDestroy();
        }
        viewControllers.clear();
    }

    /**
     * 是否可见
     *
     * @return
     */
    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public ViewGroup getContentView() {
        return rootView;
    }

    /**
     * 默认的根布局是竖直方向分为两个View：
     * 0：标题
     * 1：内容
     * ----------------------------------------------------
     * 如果你想改变这种行为，可以重写此方法。返回的布局需要满足两个条件：
     * 1、标题 tag 为：title
     * 2、内容 tag 为：content
     *
     * @return
     */
    protected View getRootView() {
        return null;
    }

    @Override
    public void initWidget() {
    }

    @Override
    public void initWidget(Intent intent) {
        initWidget();
    }

    @Override
    public void initWidget(Bundle args) {
        initWidget();
    }

    public <T extends View> T get(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = rootView.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }

    public void setOnClickListener(final View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            if (listener == null) {
                get(id).setOnClickListener(null);
            } else {
                get(id).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        if (listener != null) {
                            listener.onClick(v);
                        }
                    }
                });
            }
        }
    }

    protected <T extends Activity> T getActivity() {
        return (T) context;
    }

    public <T extends Fragment> T getFragment() {
        return (T) fragment;
    }

    protected Resources getResources() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return activity.getResources();
        } else {
            return null;
        }
    }

    protected String getString(@StringRes int strRes) {
        if (getActivity() != null) {
            return getActivity().getString(strRes);
        }
        return null;
    }

    protected String getString(@StringRes int strRes, Object... formatArgs) {
        if (getActivity() != null) {
            return getActivity().getString(strRes, formatArgs);
        }
        return null;
    }

    AbstractLoadHelper mLoadViewHelper;

    /**
     * 初始化LoadViewHelper
     * <p>默认情况，getContentLayoutId() 返回的布局会作为整个加载布局。如果你想改变，重写 getLoadView() 方法</p>
     *
     * @param view
     */
    void initLoadViewHelper(View view) {
        if (getLoadView() != null) {
            view = getLoadView();
        }
        mLoadViewHelper = getLoadViewHelper(view);
    }

    /**
     * 返回需要显示"加载"的布局
     *
     * @return
     */
    protected View getLoadView() {
        return null;
    }

    /**
     * 自定义加载、错误、无数据布局，子类重载实现定制化
     *
     * @param view
     * @return
     */
    protected abstract AbstractLoadHelper getLoadViewHelper(View view);

    public void showLoadView() {
        showLoadView(null, -1, -1);
    }

    public void showLoadView(String loadString) {
        showLoadView(loadString, -1, -1);
    }

    /**
     * 加载中页面
     */
    public void showLoadView(String loadString, @DrawableRes int loadRes, @DrawableRes int bgRes) {
        mLoadViewHelper.show(mLoadViewHelper.loadingView(loadString, loadRes, bgRes));
    }

    public void showLoadError(final View.OnClickListener listener) {
        showLoadError(null, -1, null, -1, listener);
    }

    public void showLoadError(String errMsg, final View.OnClickListener listener) {
        showLoadError(errMsg, -1, null, -1, listener);
    }

    public void showLoadError(String errMsg, @DrawableRes int bgResId, View.OnClickListener listener) {
        showLoadError(errMsg, -1, null, bgResId, listener);
    }

    /**
     * 显示加载失败
     *
     * @param errorText
     * @param errorRes
     * @param buttonText
     * @param bgRes
     * @param onClickListener
     */
    public void showLoadError(String errorText, @DrawableRes int errorRes, String buttonText, @DrawableRes int bgRes,
                              View.OnClickListener onClickListener) {
        mLoadViewHelper.show(mLoadViewHelper.errorView(errorText, errorRes, buttonText, bgRes, onClickListener));
    }

    public void showLoadEmpty() {
        showLoadEmpty(null, -1, -1, null);
    }

    public void showLoadEmpty(String emptyMsg) {
        showLoadEmpty(emptyMsg, -1, -1, null);
    }

    public void showLoadEmpty(String emptyMsg, View.OnClickListener onClickListener) {
        showLoadEmpty(emptyMsg, -1, -1, onClickListener);
    }

    public void showLoadEmpty(@DrawableRes int bgRes) {
        showLoadEmpty(null, -1, bgRes, null);
    }

    public void showLoadEmpty(View.OnClickListener onClickListener) {
        showLoadEmpty(null, -1, -1, onClickListener);
    }

    public void showLoadEmpty(String emptyMsg, @DrawableRes int emptyRes) {
        showLoadEmpty(emptyMsg, emptyRes, -1, null);
    }

    public void showLoadEmpty(String emptyMsg, @DrawableRes int emptyRes, @DrawableRes int bgRes) {
        showLoadEmpty(emptyMsg, emptyRes, bgRes, null);
    }

    public void showLoadEmpty(String emptyMsg, @DrawableRes int emptyRes, View.OnClickListener onClickListener) {
        showLoadEmpty(emptyMsg, emptyRes, -1, onClickListener);
    }

    /**
     * 显示空数据页面
     *
     * @param emptyMsg
     * @param emptyRes
     * @param onClickListener
     */
    public void showLoadEmpty(String emptyMsg, @DrawableRes int emptyRes, @DrawableRes int bgRes, View.OnClickListener onClickListener) {
        mLoadViewHelper.show(mLoadViewHelper.emptyView(emptyMsg, emptyRes, null, bgRes, onClickListener));
    }

    /**
     * 隐藏加载页面
     */
    public void hideLoadView() {
        mLoadViewHelper.hide();
    }

    /**
     * Toast
     *
     * @param message
     */
    public void showToast(CharSequence message) {
        mLoadViewHelper.showToast(message);
    }

    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     */
    public void showProgress(String message, boolean cancelable) {
        mLoadViewHelper.showProgress(message, cancelable);
    }

    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     * @param cancelListener
     */
    public void showProgress(String message, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        mLoadViewHelper.showProgress(message, cancelable, cancelListener);
    }

    /**
     * 隐藏加载框
     */
    public void hideProgress() {
        mLoadViewHelper.hideProgress();
    }

    void addViewController(ViewController viewController) {
        viewControllers.add(viewController);
    }
}
