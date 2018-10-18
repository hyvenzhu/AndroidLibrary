package library.common.framework.logic.permissions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import library.common.R;
import library.common.util.IntentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */

public class PermissionsFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    MPermissions.PermissionsCallback callback;
    
    /**
     * 待申请权限
     */
    String[] permissions;
    /**
     * 权限描述, 例如:发送短信、访问相机
     */
    String permissionDesc;

    public PermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void requestPermissions(@Nullable String permissionDesc, @NonNull String[] permissions, @NonNull MPermissions.PermissionsCallback callback) {
        this.permissionDesc = permissionDesc;
        this.permissions = permissions;
        this.callback = callback;

        if (hasAllPermissionsGranted(permissions)) {
            callback.onGranted();
        } else {
            requestPermissions();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        permissions = getAllDeniedPermissions();
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            callback.onGranted();
        } else {
            showMissingPermissionDialog();
        }
    }

    /**
     * 权限提示对话框
     */
    private void showMissingPermissionDialog() {
        String formatStr = getString(R.string.com_permission_desc_text);
        String message = String.format(formatStr, TextUtils.isEmpty(permissionDesc) ? "必要" : permissionDesc);

        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).setAction(R.string.com_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAppSettings();
            }
        }).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                // 不是点击按钮取消的
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    callback.onDenied();
                }
            }
        }).show();
    }

    /**
     * 所有权限都已通过
     *
     * @param grantResults
     * @return
     */
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 所有权限都已通过
     *
     * @param permissions
     * @return
     */
    private boolean hasAllPermissionsGranted(@NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!hasSelfPermission(getActivity(), permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine context has access to the given permission.
     * <p>
     * This is a workaround for RuntimeException of Parcel#readException.
     * For more detail, check this issue https://github.com/hotchemi/PermissionsDispatcher/issues/107
     *
     * @param context    context
     * @param permission permission
     * @return returns true if context has access to the given permission, false otherwise.
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            return hasSelfPermissionForXiaomi(context, permission);
        }
        try {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    /**
     * 小米手机如果拒绝过某一个权限，然后主动到设置中打开，还是需要再次申请该权限
     * @param context
     * @param permission
     * @return
     */
    private static boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
        if (permissionToOp == null) {
            // in case of normal permissions(e.g. INTERNET)
            return true;
        }
        int noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp, Process.myUid(), context.getPackageName());
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 返回所有被拒绝的权限
     *
     * @return
     */
    private String[] getAllDeniedPermissions() {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasSelfPermission(getActivity(), permission)) {
                deniedPermissions.add(permission);
            }
        }
        String[] permissions = new String[deniedPermissions.size()];
        deniedPermissions.toArray(permissions);
        return permissions;
    }

    /**
     * 启动应用设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            IntentUtils.startActivity(intent, getActivity());
        } else {
            callback.onDenied();
        }
    }
}
