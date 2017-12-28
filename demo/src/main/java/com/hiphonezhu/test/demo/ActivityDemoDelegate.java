package com.hiphonezhu.test.demo;

import android.support.v4.content.ContextCompat;

import com.android.baseline.framework.ui.activity.view.AppDelegate;

/**
 * author : zhuhf
 * e-mail : hiphonezhu@gmail.com
 * time   : 2017/12/21
 * desc   :
 * version: 1.0
 */
public class ActivityDemoDelegate extends AppDelegate {
    @Override
    public int getContentLayoutId() {
        return R.layout.activity_demo;
    }
    
    @Override
    public void initWidget() {
        super.initWidget();
        setBackgroundColor(ContextCompat.getColor(this.getActivity(), com.android.baseline.R.color.com_ff03a9f4));
        setTitleText("网络请求");
    }
}
