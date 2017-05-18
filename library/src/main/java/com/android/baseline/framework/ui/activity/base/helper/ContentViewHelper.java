package com.android.baseline.framework.ui.activity.base.helper;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.baseline.R;
import com.android.baseline.util.APKUtil;

/**
 * 用于实现状态栏透明效果的帮助类
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-10-07 20:52]
 */
public class ContentViewHelper {
    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    /*base view*/
    private LinearLayout mContentView;

    /*toolbar*/
    private Toolbar mToolBar;

    /*视图构造器*/
    private LayoutInflater mInflater;

    /*ToolBar是否可见*/
    private boolean mToolBarVisible;

    public ContentViewHelper(Context context, int layoutId, boolean isToolBarVisible) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mToolBarVisible = isToolBarVisible;
        /*初始化整个内容*/
        initContentView();
        /*初始化toolbar*/
        initToolBar();
         /*初始化用户定义的布局*/
        initUserView(layoutId);
    }

    /**
     * 将View延展到状态栏
     *
     * @param view
     */
    protected void setFitsSystemWindows(View view) {
        view.setFitsSystemWindows(true);
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new LinearLayout(mContext);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    private void initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        View toolbarView = mInflater.inflate(R.layout.toolbar, null);
        mToolBar = (Toolbar) toolbarView.findViewById(R.id.id_tool_bar);
        mToolBar.setVisibility(mToolBarVisible ? View.VISIBLE : View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mContentView.addView(toolbarView, params);

        if (mToolBarVisible) {
            // 延伸到状态栏
            setFitsSystemWindows(toolbarView);
        }
    }

    private void initUserView(int id) {
        View mUserView = mInflater.inflate(id, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContentView.addView(mUserView, params);
    }

    public LinearLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }
}
