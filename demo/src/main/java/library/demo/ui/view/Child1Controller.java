package library.demo.ui.view;

import android.widget.TextView;

import library.common.framework.ui.activity.view.AppDelegate;
import library.common.framework.ui.activity.view.ViewController;
import library.demo.R;

/**
 * @author zhuhf
 * @version 2019/1/23
 */
public class Child1Controller extends ViewController {

    public Child1Controller(AppDelegate appDelegate, int parentId) {
        super(appDelegate, parentId);
    }

    @Override
    public int getView() {
        return R.layout.view_child1;
    }

    public void initWidget() {
        super.initWidget();
    }
}
