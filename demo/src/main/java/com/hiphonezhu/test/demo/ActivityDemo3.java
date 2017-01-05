package com.hiphonezhu.test.demo;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.logic.permissions.MPermissions;
import com.android.baseline.framework.ui.activity.BasicActivity;

/**
 * 6.0权限
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo3 extends BasicActivity {
    private static final String TAG = "ActivityDemo3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MPermissions(ActivityDemo3.this).request("发送短信", new String[]{Manifest.permission.SEND_SMS}, new MPermissions.PermissionsCallback() {
                    @Override
                    public void onGranted() {
                        Log.i(TAG, "onGranted: ");
                    }

                    @Override
                    public void onDenied() {
                        Log.i(TAG, "onDenied: ");
                    }
                });
            }
        });

        setLeftFinish(null);
    }
}
