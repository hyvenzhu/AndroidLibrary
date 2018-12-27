package library.common.framework.logic.permissions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

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
     * 申请权限
     *
     * @param permissionDesc 需要%1$s的权限, 但此权限已被禁止, 你可以到设置中更改
     * @param permissions    权限列表
     * @param callback       回调函数
     */
    public void request(@Nullable String permissionDesc, @NonNull String[] permissions, @NonNull PermissionsCallback callback) {
        permissionsFragment.requestPermissions(permissionDesc, permissions, callback, showDenied);
    }

    /**
     * 是否所有权限都已通过
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasAllPermissionsGranted(Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!PermissionsFragment.hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public interface PermissionsCallback {
        void onGranted();

        void onDenied();
    }
}
