package com.hiphonezhu.test.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.baseline.framework.ui.activity.BasicActivity;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/9 10:02]
 */

public class MainActivity extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarFont(true);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityDemo.class));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityDemo2.class));
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityDemo3.class));
            }
        });

        setTitleBar(false, R.string.app_name, false);
    }
}
