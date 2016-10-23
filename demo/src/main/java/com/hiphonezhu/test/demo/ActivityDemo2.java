package com.hiphonezhu.test.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.baseline.framework.ui.activity.BasicActivity;

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

        Intent intent = getIntent();
        String platform = intent.getStringExtra("platform");
        int year = intent.getIntExtra("year", 0);
        Log.e("platform: ", platform);
        Log.e("year: ", String.valueOf(year));

        setResult(RESULT_OK);
    }

    @Override
    protected boolean defaultTitleBarVisible() {
        return false;
    }
}
