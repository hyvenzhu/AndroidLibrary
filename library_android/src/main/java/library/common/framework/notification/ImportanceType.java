package library.common.framework.notification;

import androidx.annotation.IntDef;

/**
 * 通知重要级别
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018/10/26]
 */
@IntDef({ImportanceType.IMPORTANCE_NONE,
        ImportanceType.IMPORTANCE_MIN,
        ImportanceType.IMPORTANCE_LOW,
        ImportanceType.IMPORTANCE_DEFAULT,
        ImportanceType.IMPORTANCE_HIGH})
public @interface ImportanceType {
    int IMPORTANCE_NONE = 0;
    int IMPORTANCE_MIN = 1;
    int IMPORTANCE_LOW = 2;
    int IMPORTANCE_DEFAULT = 3;
    int IMPORTANCE_HIGH = 4;
}
