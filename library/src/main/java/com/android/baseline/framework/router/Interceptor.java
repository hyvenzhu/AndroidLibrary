package com.android.baseline.framework.router;

import android.content.Context;
import android.os.Bundle;

/**
 * Interceptor before actual executor
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/22 10:25]
 */

public interface Interceptor {
    /**
     * return true to intercept executor
     * @param context
     * @param className Dest class path
     * @param bundle intent extras
     * @return
     */
    boolean intercept(Context context, String className, Bundle bundle);
}
