package com.hiphonezhu.test.demo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.android.baseline.framework.logic.permissions.NeedPermission;
import com.android.baseline.framework.ui.activity.BasicActivity;

import java.io.File;

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
                camera();
            }
        });

        setLeftFinish(null);
    }

    @NeedPermission(permissions = {Manifest.permission.CAMERA}, rationalMessage = "使用相机")
    public void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalCacheDir() + "photo.jpg")));
        startActivity(intent);
    }
}
