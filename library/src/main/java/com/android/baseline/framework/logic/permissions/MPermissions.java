package com.android.baseline.framework.logic.permissions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * M运行时权限
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/1/5 11:02]
 */

public class MPermissions {
    private static final String TAG = "PermissionsFragment";
    PermissionsFragment permissionsFragment;

    public MPermissions(@NonNull FragmentActivity activity) {
        permissionsFragment = (PermissionsFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (permissionsFragment == null) {
            permissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
    }

    /**
     * 申请权限
     * @param permissionDesc 需要%1$s的权限, 但此权限已被禁止, 你可以到设置中更改
     * @param permissions 权限列表
     * @param callback 回调函数
     */
    public void request(@Nullable String permissionDesc, @NonNull String[] permissions, @NonNull PermissionsCallback callback) {
        permissionsFragment.requestPermissions(permissionDesc, permissions, callback);
    }

    public interface PermissionsCallback {
        void onGranted();

        void onDenied();
    }
}
