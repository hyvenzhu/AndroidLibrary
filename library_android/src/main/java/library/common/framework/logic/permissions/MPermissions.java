package library.common.framework.logic.permissions;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    /**
     * @param activity
     * @param showDenied 是否提示权限被拒绝
     */
    public MPermissions(@NonNull FragmentActivity activity, boolean showDenied) {
        this.showDenied = showDenied;
        permissionsFragment = (PermissionsFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (permissionsFragment == null) {
            permissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }

    /**
     * @param fragment
     */
    public MPermissions(@NonNull Fragment fragment) {
        this(fragment, true);

    }

    /**
     * @param fragment
     * @param showDenied 是否提示权限被拒绝
     */
    public MPermissions(@NonNull Fragment fragment, boolean showDenied) {
        this.showDenied = showDenied;
        permissionsFragment = (PermissionsFragment) fragment.getChildFragmentManager().findFragmentByTag(TAG);
        if (permissionsFragment == null) {
            permissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = fragment.getChildFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }

    /**
     * 申请权限
     *
     * @param permissionDesc 需要%1$s的权限, 但此权限已被禁止, 你可以到设置中更改
     * @param permissions    权限列表
     * @param callback       回调函数
     */
    public void request(@Nullable String permissionDesc, @NonNull String[] permissions, @NonNull PermissionsCallback callback) {
        permissionsFragment.requestPermissions(permissionDesc, permissions, callback, showDenied);
    }

    public static boolean hasAllPermissionsGranted(@NonNull Context context,
                                                   @Size(min = 1) @NonNull String... perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    public interface PermissionsCallback {
        void onGranted();

        void onDenied();
    }
}
