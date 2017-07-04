package com.hiphonezhu.test.demo;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.logic.permissions.NeedPermission;
import com.android.baseline.framework.logic.photo.CapturePhotoHelper;
import com.android.baseline.util.APKUtil;

import java.io.File;

import common.MyBaseActivity;

/**
 * 6.0权限
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo3 extends MyBaseActivity {
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
        new CapturePhotoHelper(this)
                .camera(APKUtil.getSupportUri(this, new File(getExternalCacheDir() + "/photo.jpg")))
                .crop(50, 50, Uri.fromFile(new File(getExternalCacheDir() + "/photo_crop.jpg")))
                .capture(new CapturePhotoHelper.Callback() {
                    @Override
                    public void onSuccess(String path) {
                        Log.d(TAG, path);
                    }

                    @Override
                    public void onFailure() {

                    }
                });

    }
}
