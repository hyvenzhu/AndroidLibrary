package com.hiphonezhu.test.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.router.IntentWrapper;
import com.android.baseline.framework.router.Interceptor;
import com.android.baseline.framework.router.LiteRouter;
import com.android.baseline.framework.ui.activity.BasicActivity;
import com.hiphonezhu.test.demo.router.IntentService;

import java.util.Random;

/**
 * 路由测试
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo4 extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4);

        LiteRouter liteRouter = new LiteRouter.Builder().interceptor(new Interceptor() {
            @Override
            public boolean intercept(IntentWrapper intentWrapper) {
                return false;
            }
        }).build();
        final IntentService intentService = liteRouter.create(IntentService.class, ActivityDemo4.this);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentService.intent2ActivityDemo2("android", 2016 + new Random().nextInt(10));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentWrapper intentWrapper = intentService.intent2ActivityDemo2Raw("android", 2016);
                // intent
                Intent intent = intentWrapper.getIntent();
                // add your flags
                intentWrapper.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // call
                intentWrapper.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode: ", String.valueOf(requestCode));
        Log.e("resultCode: ", String.valueOf(resultCode));
    }
}
