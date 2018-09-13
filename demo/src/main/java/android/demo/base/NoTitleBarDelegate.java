package android.demo.base;

import android.view.View;

import library.common.framework.ui.activity.view.AppDelegate;


/**
 * 不带标题栏
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public abstract class NoTitleBarDelegate extends AppDelegate {
    @Override
    protected View getTitleView() {
        return null;
    }
}
