package com.hiphonezhu.test.demo;

import android.os.Bundle;

import com.android.baseline.framework.ui.BasicActivity;

/**
 * 默认标题栏不可见
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo2 extends BasicActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
    }

    @Override
    protected boolean defaultTitleBarVisible() {
        return false;
    }
}
