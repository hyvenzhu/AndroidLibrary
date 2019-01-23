package library.demo.base;

import android.view.View;
import android.view.ViewGroup;

import library.common.framework.ui.activity.view.AppDelegate;


/**
 * 不带标题栏
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public abstract class NoTitleBarDelegate extends AppDelegate {
    @Override
    public View getTitleView(ViewGroup root) {
        return null;
    }
}
