package library.demo.ui.view;

import android.content.Intent;

import library.demo.R;
import library.demo.base.CommonTitleBarDelegate;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public class MainDelegate extends CommonTitleBarDelegate {
    Child1Controller child1;

    @Override
    public int getContentLayoutId() {
        return R.layout.demo_activity_main;
    }

    @Override
    public void initWidget(Intent intent) {
        super.initWidget(intent);
        // 拆分多个子View
        child1 = new Child1Controller(this, R.id.group1);
        Child2Controller child2 = new Child2Controller(this, R.id.group1);
    }
}
