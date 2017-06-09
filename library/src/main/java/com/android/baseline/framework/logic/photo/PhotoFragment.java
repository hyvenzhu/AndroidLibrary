package com.android.baseline.framework.logic.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.baseline.util.APKUtil;
import com.android.baseline.util.IntentUtil;

/**
 * 处理拍照、裁剪的不可见Fragment
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/1/5 15:31]
 */

public class PhotoFragment extends Fragment {
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_ABLUM = 2;
    public static final int REQUEST_CODE_CROP = 3;

    int outputX;
    int outputY;
    CapturePhotoHelper.Callback callback;
    Uri uri;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void camera(Intent intent, int outputX, int outputY, CapturePhotoHelper.Callback callback) {
        uri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        start(intent, REQUEST_CODE_CAMERA, outputX, outputY, callback);
    }

    void album(Intent intent, int outputX, int outputY, CapturePhotoHelper.Callback callback) {
        start(intent, REQUEST_CODE_ABLUM, outputX, outputY, callback);
    }

    void start(Intent intent, int requestCode, int outputX, int outputY, CapturePhotoHelper.Callback callback) {
        this.outputX = outputX;
        this.outputY = outputY;
        this.callback = callback;
        IntentUtil.startActivityForResult(intent, this, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    onNext();
                } else {
                    callback.onFailure();
                }
                break;
            case REQUEST_CODE_ABLUM:
                if (resultCode == Activity.RESULT_OK) {
                    uri = data.getData();
                    onNext();
                } else {
                    callback.onFailure();
                }
                break;
            case REQUEST_CODE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    callback.onSuccess(APKUtil.getRealFilePath(getActivity(), uri));
                } else {
                    callback.onFailure();
                }
                break;
        }
    }

    /**
     * 是否裁剪
     */
    void onNext() {
        if (outputX != 0 && outputY != 0) {
            crop(uri, outputX, outputY, REQUEST_CODE_CROP);
        } else {
            callback.onSuccess(APKUtil.getRealFilePath(getActivity(), uri));
        }
    }

    /**
     * 图片裁剪
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public void crop(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", outputX);
        intent.putExtra("aspectY", outputY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        intent = APKUtil.getSupportIntent(intent, true);
        IntentUtil.startActivityForResult(intent, this, requestCode);
    }
}
