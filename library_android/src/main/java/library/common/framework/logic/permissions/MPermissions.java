package library.common.framework.logic.permissions;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * M运行时权限
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */

public class MPermissions {
    private static final String TAG = "PermissionsFragment";
    PermissionsFragment permissionsFragment;
    boolean showDenied;

    public MPermissions(@NonNull FragmentActivity activity) {
        this(activity, true);
    }

    public MPermissions(@NonNull FragmentActivity activity, boolean showDenied) {
        this(activity.getSupportFragmentManager(), showDenied);
    }

    public MPermissions(@NonNull Fragment fragment) {
        this(fragment, true);
    }

    public MPermissions(@NonNull Fragment fragment, boolean showDenied) {
        this(fragment.getChildFragmentManager(), showDenied);
    }

    public MPermissions(@NonNull FragmentManager manager, boolean showDenied) {
        this.showDenied = showDenied;
        permissionsFragment = (PermissionsFragment) manager.findFragmentByTag(TAG);
        if (permissionsFragment == null) {
            permissionsFragment = new PermissionsFragment();
            manager.beginTransaction().add(permissionsFragment, TAG).commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
    }

    /**
     * 申请权限
     *
     * @param permissionDesc 权限描述，例如：录音、相机
     * @param permissions    权限列表
     * @param callback       回调函数
     */
    public void request(@NonNull String permissionDesc, @NonNull String[] permissions, @NonNull PermissionsCallback callback) {
        permissionsFragment.requestPermissions(permissionDesc, permissions, callback, showDenied);
    }

    public static boolean hasAllPermissionsGranted(@NonNull Context context,
                                                   @Size(min = 1) @NonNull String[] perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    public interface PermissionsCallback {
        void onGranted();

        void onDenied();
    }
}
