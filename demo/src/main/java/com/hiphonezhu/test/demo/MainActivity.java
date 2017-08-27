package com.hiphonezhu.test.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.baseline.framework.ui.activity.BasicActivity;

import butterknife.OnClick;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/9 10:02]
 */

public class MainActivity extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarFontColor(true);

        setTitleBar(false, R.string.app_name, false);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this, ActivityDemo.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, ActivityDemo2.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(MainActivity.this, ActivityDemo3.class));
                break;
        }
    }
}
