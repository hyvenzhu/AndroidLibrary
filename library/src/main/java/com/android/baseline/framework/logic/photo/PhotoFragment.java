package com.android.baseline.framework.logic.photo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.baseline.R;
import com.android.baseline.util.APKUtil;
import com.android.baseline.util.IntentUtil;
import com.yalantis.ucrop.UCrop;

/**
 * 处理拍照、裁剪的不可见Fragment
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/1/5 15:31]
 */

public class PhotoFragment extends Fragment {
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_ABLUM = 2;

    Uri cameraOutputUri;
    Uri cropOutputUri;
    int outputX;
    int outputY;
    CapturePhotoHelper.Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void camera(Intent intent, Uri cropOutputUri, int outputX, int outputY, CapturePhotoHelper.Callback callback) {
        cameraOutputUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        start(intent, REQUEST_CODE_CAMERA, cropOutputUri, outputX, outputY, callback);
    }

    void album(Intent intent, Uri cropOutputUri, int outputX, int outputY, CapturePhotoHelper.Callback callback) {
        start(intent, REQUEST_CODE_ABLUM, cropOutputUri, outputX, outputY, callback);
    }

    void start(Intent intent, int requestCode, Uri cropOutputUri, int outputX, int outputY, CapturePhotoHelper.Callback callback) {
        this.cropOutputUri = cropOutputUri;
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
                    cameraOutputUri = data.getData();
                    onNext();
                } else {
                    callback.onFailure();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);
                    callback.onSuccess(APKUtil.getRealFilePath(getActivity(), resultUri));
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
            crop(cameraOutputUri, cropOutputUri, outputX, outputY);
        } else {
            callback.onSuccess(APKUtil.getRealFilePath(getActivity(), cameraOutputUri));
        }
    }

    /**
     * 图片裁剪
     *
     * @param inputUri
     * @param outputX
     * @param outputY
     */
    public void crop(Uri inputUri, Uri outputUri, int outputX, int outputY) {
        // 使用 uCrop 裁剪库
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setShowCropGrid(false);
        options.setStatusBarColor(getResources().getColor(R.color.title_bar_color));
        options.setToolbarColor(getResources().getColor(R.color.title_bar_color));

        float ratioX = Float.valueOf(outputX);
        float ratioY = Float.valueOf(outputY);
        UCrop.of(inputUri, outputUri)
                .withAspectRatio(ratioX, ratioY)
                .withMaxResultSize(outputX, outputY)
                .withOptions(options)
                .start(getActivity(), this);
    }
}
