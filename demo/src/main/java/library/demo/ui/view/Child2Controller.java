package library.demo.ui.view;

import library.common.framework.ui.activity.view.AppDelegate;
import library.common.framework.ui.activity.view.ViewController;
import library.demo.R;

/**
 * @author zhuhf
 * @version 2019/1/23
 */
public class Child2Controller extends ViewController {

    public Child2Controller(AppDelegate appDelegate, int parentId) {
        super(appDelegate, parentId);
    }

    @Override
    public int getView() {
        return R.layout.view_child2;
    }
}
