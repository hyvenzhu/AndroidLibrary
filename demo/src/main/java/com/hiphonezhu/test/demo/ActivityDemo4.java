package com.hiphonezhu.test.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.router.Interceptor;
import com.android.baseline.framework.router.LiteRouter;
import com.android.baseline.framework.ui.activity.BasicActivity;
import com.hiphonezhu.test.demo.router.IntentService;

/**
 * 路由测试
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo4 extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4);

        // 网络请求
        findViewById(R.id.net_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LiteRouter.Builder().interceptor(new Interceptor() {
                    @Override
                    public boolean intercept(Context context, String className, Bundle bundle) {
                        return false;
                    }
                }).build().create(IntentService.class, ActivityDemo4.this).intent2ActivityDemo2("android", 2016);
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
