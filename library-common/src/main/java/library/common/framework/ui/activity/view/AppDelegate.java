/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package library.common.framework.ui.activity.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import library.common.R;
import library.common.framework.ui.widget.AbstractLoadHelper;
import library.common.framework.ui.widget.CommonTitleBar;
import library.common.framework.ui.widget.DefaultLoadHelper;
import library.common.util.Callback;

import butterknife.ButterKnife;

/**
 * View delegate base class
 * 视图层代理的基类
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class AppDelegate implements IDelegate {
    protected final SparseArray<View> mViews = new SparseArray<View>();
    
    protected ViewGroup rootView;
    ViewGroup titleGroup;
    Context context;
    Fragment fragment;
    View titleView;
    CommonTitleBar.OnTitleBarClickListener titleBarClickListener;
    View.OnClickListener titleTextClickListener;
    
    Callback callback;
    
    boolean isVisible;
    
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
    
    public abstract int getContentLayoutId();
    
    @Override
    public void create(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = context;
        rootView = (ViewGroup) getRootView();
        if (rootView == null) {
            rootView = (ViewGroup) inflater.inflate(R.layout.com_activity_common, container, false);
        }
        titleGroup = rootView.findViewWithTag("title");
        ViewGroup content = rootView.findViewWithTag("content");
        
        titleView = getTitleView();
        if (titleView != null) {
            titleGroup.setVisibility(View.VISIBLE);
            titleGroup.addView(titleView);
        } else {
            titleGroup.setVisibility(View.GONE);
        }
        
        // 内容布局
        int contentLayoutId = getContentLayoutId();
        if (contentLayoutId > 0) {
            View contentView = inflater.inflate(contentLayoutId, container, false);
            content.addView(contentView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            
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
    }
    
    @Override
    public void onHide() {
        isVisible = false;
    }
    
    @Override
    public void onDestroy() {
    
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
    
    /**
     * 返回标题栏，通过重写自定义标题栏
     *
     * @return
     */
    protected View getTitleView() {
        CommonTitleBar commonTitleBar = new CommonTitleBar(this.getActivity());
        commonTitleBar.setOnTitleBarClickListener(new CommonTitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftClick(View v) {
                if (titleBarClickListener != null) {
                    titleBarClickListener.onLeftClick(v);
                } else {
                    getActivity().finish();
                }
            }
            
            @Override
            public void onRightClick(View v) {
                if (titleBarClickListener != null) {
                    titleBarClickListener.onRightClick(v);
                }
            }
        });
        commonTitleBar.setOnTitleTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleTextClickListener != null) {
                    titleTextClickListener.onClick(v);
                }
            }
        });
        commonTitleBar.setBackgroundColor(ContextCompat.getColor(this.getActivity(), R.color.com_ff03a9f4));
        commonTitleBar.setLeftImage(R.drawable.com_back_bg);
        return commonTitleBar;
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
    public View getContentView() {
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
    public View getRootView() {
        return null;
    }
    
    @Override
    public void initWidget() {
    }
    
    public <T extends View> T bindView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) rootView.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }
    
    public <T extends View> T get(int id) {
        return (T) bindView(id);
    }
    
    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(listener);
        }
    }
    
    public <T extends Activity> T getActivity() {
        return (T) context;
    }
    
    public <T extends Fragment> T getFragment() {
        return (T) fragment;
    }
    
    public Resources getResources() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return activity.getResources();
        } else {
            return null;
        }
    }
    
    public String getString(@StringRes int strRes) {
        if (getActivity() != null) {
            return getActivity().getString(strRes);
        }
        return null;
    }
    
    public void setBackgroundColor(@ColorInt int color) {
        titleView.setBackgroundColor(color);
    }
    
    public void setTitleText(@StringRes int txtResId) {
        setTitleText(getString(txtResId));
    }
    
    public void setTitleText(String txt) {
        if (titleView instanceof CommonTitleBar) {
            ((CommonTitleBar) titleView).setTitleText(txt);
        }
    }
    
    public void setLeftImage(@DrawableRes int imgResId) {
        if (titleView instanceof CommonTitleBar) {
            ((CommonTitleBar) titleView).setLeftImage(imgResId);
        }
    }
    
    public void setRightImage(@DrawableRes int imgResId) {
        if (titleView instanceof CommonTitleBar) {
            ((CommonTitleBar) titleView).setRightImage(imgResId);
        }
    }
    
    public void setOnTitleBarClickListener(CommonTitleBar.OnTitleBarClickListener titleBarClickListener) {
        this.titleBarClickListener = titleBarClickListener;
    }
    
    public void setOnTitleTextClickListener(View.OnClickListener titleTextClickListener) {
        this.titleTextClickListener = titleTextClickListener;
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
    protected AbstractLoadHelper getLoadViewHelper(View view) {
        return new DefaultLoadHelper(view);
    }
    
    public void showLoadView() {
        showLoadView(-1);
    }
    
    /**
     * 加载中页面
     */
    public void showLoadView(@DrawableRes int bgRes) {
        mLoadViewHelper.show(mLoadViewHelper.loadingView(getString(R.string.com_prompt_loading), -1, bgRes));
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
     * 隐藏加载框
     */
    public void hideProgress() {
        mLoadViewHelper.hideProgress();
    }
}
