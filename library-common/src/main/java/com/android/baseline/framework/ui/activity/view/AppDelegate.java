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
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baseline.R;
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
    
    private static Toast mToast;
    
    public abstract int getRootLayoutId();
    
    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        rootView = inflater.inflate(rootLayoutId, container, false);
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
    
    LoadViewHelper mLoadViewHelper;
    
    /**
     * 初始化LoadViewHelper
     *
     * @param view
     */
    public void initLoadViewHelper(View view) {
        mLoadViewHelper = new LoadViewHelper(view);
    }
    
    /**
     * 加载中页面
     */
    public void showLoadView() {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.showLoading(getString(R.string.com_prompt_loading));
        }
    }
    
    /**
     * 隐藏加载页面
     */
    public void hideLoadView() {
        if (mLoadViewHelper != null) {
            mLoadViewHelper.restore();
        }
    }
    
    public void showLoadError(final View.OnClickListener listener) {
        showLoadError(getString(R.string.loading_error_txt), listener);
    }
    
    public void showLoadError(String errMsg, final View.OnClickListener listener) {
        showLoadError(TextUtils.isEmpty(errMsg) ? getString(R.string.loading_error_txt) : errMsg, getString(R.string.reload), R.drawable.com_ic_loading_error, listener);
    }
    
    /**
     * 显示加载失败
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
            customDialog = new CustomDialog(rootView.getContext()).setContentView(R.layout.dialog_loading)
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
