package library.demo.ui;

import library.demo.R;
import library.demo.base.BaseActivity;
import library.demo.logic.ModuleALogic;
import library.demo.ui.view.MainDelegate;
import android.view.View;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public class MainActivity extends BaseActivity<MainDelegate> {
    @Override
    protected Class getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    protected Object beforeOnChanged(Object res) {
        return null;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        final ModuleALogic logic = findLogic(new ModuleALogic(this));

        viewDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logic.categoryList();
            }
        }, R.id.btn);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSuccess(int requestId, Object response, String responseCode) {
        super.onSuccess(requestId, response, responseCode);
        if (requestId == R.id.demo_id) {
        }
    }

    @Override
    protected void onFailure(int requestId, Object response, String responseCode, String errmsg) {
        super.onFailure(requestId, response, responseCode, errmsg);
        if (requestId == R.id.demo_id) {
        }
    }
}
