package library.demo.base;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import library.common.framework.ui.activity.view.AppDelegate;
import library.common.framework.ui.widget.AbstractLoadHelper;
import library.demo.R;


/**
 * 全局统一标题栏
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public abstract class CommonTitleBarDelegate extends AppDelegate {

    @Override
    public View getTitleView(ViewGroup root) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.layout_common_title, root);
    }

    @Override
    protected Class getViewBindClass() {
        return null;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setCommonTitleColor(Color.parseColor("#ffffff"));
        setLightMode(getActivity());
    }

    @Override
    protected AbstractLoadHelper getLoadViewHelper(View view) {
        return null;
    }
}
