package android.demo.ui.view;

import android.demo.R;
import android.demo.base.CommonTitleBarDelegate;
import android.view.View;

import library.common.framework.ui.widget.AbstractLoadHelper;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public class MainDelegate extends CommonTitleBarDelegate {
    @Override
    public int getContentLayoutId() {
        return R.layout.demo_activity_main;
    }
    
    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    protected AbstractLoadHelper getLoadViewHelper(View view) {
        return null;
    }
}
