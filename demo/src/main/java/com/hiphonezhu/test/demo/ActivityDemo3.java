package com.hiphonezhu.test.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.ui.activity.BasicActivity;
import com.android.baseline.framework.ui.activity.PermissionsActivity;

/**
 * 6.0权限
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo3 extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsActivity.actionStartForResult(ActivityDemo3.this, 100, "发送短信", new String[]{Manifest.permission.SEND_SMS});
            }
        });

        setLeftFinish(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                    Log.d("ActivityDemo3", "权限申请成功");
                } else {
                    Log.d("ActivityDemo3", "权限申请失败");
                }
                break;
        }
    }
}
