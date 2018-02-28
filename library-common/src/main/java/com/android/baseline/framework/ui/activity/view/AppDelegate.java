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
package com.android.baseline.framework.ui.activity.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.baseline.R;
import com.android.baseline.framework.ui.widget.CommonTitleBar;
import com.android.baseline.framework.ui.widget.CustomDialog;
import com.android.baseline.framework.ui.widget.LoadViewHelper;

/**
 * View delegate base class
 * 视图层代理的基类
 *
 * @author kymjs (http://www.kymjs.com/) on 10/23/15.
 */
public abstract class AppDelegate implements IDelegate {
    protected final SparseArray<View> mViews = new SparseArray<View>();
    
    protected View rootView;
    View titleView;
    CommonTitleBar.OnTitleBarClickListener titleBarClickListener;
    View.OnClickListener titleTextClickListener;
    ViewSwitcher content;
    
    private static Toast mToast;
    
    public abstract int getContentLayoutId();
    
    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.com_activity_common, container, false);
        ViewGroup title = rootView.findViewById(R.id.title);
        content = rootView.findViewById(R.id.content);
        
        titleView = getTitleView();
        if (titleView != null) {
            title.setVisibility(View.VISIBLE);
            title.addView(titleView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            title.setVisibility(View.GONE);
        }
        
        // 内容布局
        int rootLayoutId = getContentLayoutId();
        View contentView = inflater.inflate(rootLayoutId, container, false);
        content.addView(contentView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 刷新布局
        View refreshView = new View(this.getActivity());
        refreshView.setBackgroundColor(ContextCompat.getColor(this.getActivity(), R.color.com_ff03a9f4));
        content.addView(refreshView, 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        
        // 默认显示内容布局
        content.setDisplayedChild(0);
        // 初始化加载布局
        initLoadViewHelper(refreshView);
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
    public View getRootView() {
        return rootView;
    }
    
    public void setRootView(View rootView) {
        this.rootView = rootView;
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
        return (T) rootView.getContext();
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
    
    LoadViewHelper mLoadViewHelper;
    
    /**
     * 初始化LoadViewHelper
     *
     * @param view
     */
    void initLoadViewHelper(View view) {
        mLoadViewHelper = new LoadViewHelper(view);
    }
    
    /**
     * 加载中页面
     */
    public void showLoadView() {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.showLoading(getString(R.string.com_prompt_loading));
        }
        content.setDisplayedChild(1);
    }
    
    /**
     * 隐藏加载页面
     */
    public void hideLoadView() {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.restore();
        }
        content.setDisplayedChild(0);
    }
    
    public void showLoadError(final View.OnClickListener listener) {
        showLoadError(getString(R.string.com_loading_error_txt), listener);
    }
    
    public void showLoadError(String errMsg, final View.OnClickListener listener) {
        showLoadError(TextUtils.isEmpty(errMsg) ? getString(R.string.com_loading_error_txt) : errMsg, getString(R.string.com_reload), R.drawable.com_ic_loading_error, listener);
    }
    
    /**
     * 显示加载失败
     *
     * @param errorText
     * @param buttonText
     * @param resId
     * @param onClickListener
     */
    public void showLoadError(String errorText, String buttonText, @DrawableRes int resId,
                              View.OnClickListener onClickListener) {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.showError(errorText, buttonText, resId, onClickListener);
        }
        content.setDisplayedChild(1);
    }
    
    public void showLoadEmpty() {
        showLoadEmpty(getString(R.string.com_nodata), R.drawable.com_nodata);
    }
    
    /**
     * 显示空数据页面
     */
    public void showLoadEmpty(String emptyMsg, int imageId) {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.showEmpty(emptyMsg, imageId);
        }
        content.setDisplayedChild(1);
    }
    
    public void showLayout(int layoutId) {
        showLayout(getRootView().findViewById(layoutId));
    }
    
    /**
     * 显示特定布局
     */
    public void showLayout(View layout) {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.showLayout(layout);
        }
        content.setDisplayedChild(1);
    }
    
    public void showToast(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(rootView.getContext().getApplicationContext(),
                    message,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }
    
    CustomDialog customDialog;
    TextView tipTextView;
    
    public void showProgress(String message, boolean cancelable) {
        if (customDialog == null) {
            customDialog = new CustomDialog(rootView.getContext()).setContentView(R.layout.com_dialog_loading)
                    .setCancelable(cancelable)
                    .setCanceledOnTouchOutside(false)
                    .create();
        } else {
            customDialog.dismiss();
        }
        customDialog.getDialog().setCancelable(cancelable);
        customDialog.show();
        
        if (tipTextView == null) {
            tipTextView = (TextView) customDialog.findViewById(R.id.tipTextView);
        }
        
        if (!TextUtils.isEmpty(message)) {
            tipTextView.setText(message);
        } else {
            tipTextView.setText("数据加载中...");
        }
    }
    
    public void hideProgress() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }
}
