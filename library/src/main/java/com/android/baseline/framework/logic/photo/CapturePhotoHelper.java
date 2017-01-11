package com.android.baseline.framework.logic.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.android.baseline.util.APKUtil;

import java.io.File;

/**
 * 从相机、相册选取照片
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/1/5 14:11]
 */
public class CapturePhotoHelper {
    private static final String TAG = "CapturePhotoHelper";
    String authority;
    PhotoFragment photoFragment;

    File savedDir;
    int outputX;
    int outputY;

    public CapturePhotoHelper(@NonNull FragmentActivity activity) {
        photoFragment = getPhotoFragment(activity);
    }

    PhotoFragment getPhotoFragment(FragmentActivity activity) {
        PhotoFragment photoFragment = (PhotoFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (photoFragment == null) {
            photoFragment = new PhotoFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(photoFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return photoFragment;
    }

    /**
     * 从相机选取
     *
     * @param savedDir
     * @param authority
     * @return
     */
    public CapturePhotoHelper camera(@NonNull File savedDir, String authority) {
        this.savedDir = savedDir;
        this.authority = authority;
        return this;
    }

    /**
     * 从相册选取
     *
     * @return
     */
    public CapturePhotoHelper album() {
        return this;
    }

    /**
     * 裁剪
     *
     * @param outputX
     * @param outputY
     * @return
     */
    public CapturePhotoHelper crop(int outputX, int outputY) {
        this.outputX = outputX;
        this.outputY = outputY;
        return this;
    }

    /**
     * 选取
     *
     * @param callback
     */
    public void capture(Callback callback) {
        if (savedDir != null) {
            File savedFile = new File(savedDir, System.currentTimeMillis() + ".jpg");
            Uri uri = APKUtil.getSupportUri(authority, savedFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (Build.VERSION.SDK_INT >= 24) {
                // 授予目录临时共享权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            photoFragment.camera(intent, outputX, outputY, callback);
        } else {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            photoFragment.album(intent, outputX, outputY, callback);
        }
    }

    public interface Callback {
        void onSuccess(String path);
        void onFailure();
    }
}
