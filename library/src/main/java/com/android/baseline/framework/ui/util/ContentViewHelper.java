package com.android.baseline.framework.ui.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.baseline.R;

/**
 * 用于实现沉浸式效果的帮助类
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-10-07 20:52]
*/
public class ContentViewHelper {
    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    /*base view*/
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    /*toolbar*/
    private Toolbar mToolBar;

    /*视图构造器*/
    private LayoutInflater mInflater;

    /*ToolBar是否可见*/
    private boolean mToolBarVisible;

    /*ToolBar颜色*/
    private int mToolBarColor;

    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private static int[] ATTRS = {
            R.attr.actionBarSize,
    };

    public ContentViewHelper(Context context, int layoutId) {
        this(context, layoutId, true, context.getResources().getColor(R.color.primary));
    }

    public ContentViewHelper(Context context, int layoutId, boolean isToolBarVisible, int toolBarColor) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mToolBarVisible = isToolBarVisible;
        this.mToolBarColor = toolBarColor;
        /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initUserView(layoutId);
        /*初始化toolbar*/
        initToolBar();
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // 不设置的话ToolBar会陷到状态栏里面去
        mContentView.setFitsSystemWindows(true);
        // 设置颜色和ToolBar一致
        mContentView.setBackgroundColor(mToolBarColor);
        mContentView.setLayoutParams(params);
    }

    /**
     * 设置整个Window的背景(状态栏+导航栏+Layout)
     * tips: 4.4以下状态栏无效果
     * @param resid
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setWindowBackground(int resid)
    {
        mContentView.setBackgroundResource(resid);
        // Layout布局的背景设为透明(否则看不到window设置的背景)
        if (resid != 0)
        {
            mUserView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(R.layout.toolbar, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.id_tool_bar);
        mToolBar.setVisibility(mToolBarVisible? View.VISIBLE : View.GONE);
    }

    private void initUserView(int id) {
        mUserView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) typedArray.getDimension(0,(int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));

        /*ToolBar不可见也不设置间距*/
        params.topMargin = !mToolBarVisible ? 0 : toolBarSize;
        // 用户的布局背景
        Drawable bgDrawable = mUserView.getBackground();
        if (bgDrawable == null)
        {
            mUserView.setBackgroundColor(Color.WHITE);
        }
        typedArray.recycle();
        mContentView.addView(mUserView, params);
    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }
}
