package library.common.framework.logic.permissions;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import library.common.R;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */

public class PermissionsFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    MPermissions.PermissionsCallback permissionsCallback;

    /**
     * 待申请权限
     */
    String[] permissions;
    /**
     * 权限描述, 例如:发送短信、访问相机
     */
    String permissionDesc;
    boolean showDenied;

    public PermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void requestPermissions(@Nullable String permissionDesc, @NonNull String[] permissions, @NonNull final MPermissions.PermissionsCallback callback, boolean showDenied) {
        this.permissionDesc = permissionDesc;
        this.permissions = permissions;
        this.permissionsCallback = callback;
        this.showDenied = showDenied;

        if (EasyPermissions.hasPermissions(requireActivity(), permissions)) {
            // Already have permission, do the thing
            callback.onGranted();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, null,
                    PERMISSIONS_REQUEST_CODE, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.hasPermissions(requireContext(), permissions) && permissionsCallback != null) {
            permissionsCallback.onGranted();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (showDenied) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(R.string.com_permission_title_text)
                    .setRationale(getString(R.string.com_permission_desc_text, permissionDesc))
                    .setRequestCode(PERMISSIONS_REQUEST_CODE)
                    .build().show();
        } else if (permissionsCallback != null) {
            permissionsCallback.onDenied();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            // Do something after user returned from app settings screen
            if (permissionsCallback != null) {
                if (EasyPermissions.hasPermissions(requireContext(), permissions)) {
                    permissionsCallback.onGranted();
                } else {
                    permissionsCallback.onDenied();
                }
            }
        }
    }
}
