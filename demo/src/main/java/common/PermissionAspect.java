package common;

import com.android.baseline.AppDroid;
import com.android.baseline.framework.logic.permissions.MPermissions;
import com.android.baseline.framework.logic.permissions.NeedPermission;
import com.android.baseline.framework.ui.activity.BasicActivity;
import com.android.baseline.framework.ui.util.UIStateHelper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 使用 AOP 实现的 M 权限申请
 * @author hiphonezhu@gmail.com
 * @version [PermissionAspect, 17/1/12 14:17]
 */
@Aspect
public class PermissionAspect {
    @Pointcut("execution(@com.android.baseline.framework.logic.permissions.NeedPermission * *(..)) && @annotation(permission)")
    public void methodAnnotatedWithMPermission(NeedPermission permission) {}

    @Around("methodAnnotatedWithMPermission(permission)")
    public void checkPermission(final ProceedingJoinPoint joinPoint, NeedPermission permission) throws Throwable {
        // 权限申请
        UIStateHelper uiHelper = AppDroid.getInstance().uiStateHelper;
        BasicActivity topActivity = uiHelper.getTopActivity();
        if (topActivity != null) {
            new MPermissions(topActivity).request(permission.rationalMessage(), permission.permissions(),
                    new MPermissions.PermissionsCallback() {
                        @Override
                        public void onGranted() {
                            try {
                                // 继续执行原方法
                                joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }

                        @Override
                        public void onDenied() {

                        }
                    });
        }
    }
}
