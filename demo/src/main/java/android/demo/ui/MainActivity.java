package android.demo.ui;

import android.view.View;

import android.demo.R;
import android.demo.base.BaseActivity;
import android.demo.logic.ModuleALogic;
import android.demo.ui.view.MainDelegate;


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
    protected void onCreate() {
        super.onCreate();
        
        final ModuleALogic logic = findLogic(new ModuleALogic(this));
        
        viewDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelegate.showLoadView();
                logic.categoryList();
            }
        }, R.id.btn);
    }
    
    @Override
    protected void onSuccess(int requestId, Object response, String responseCode) {
        super.onSuccess(requestId, response, responseCode);
        if (requestId == R.id.demo_id) {
            viewDelegate.hideLoadView();
            viewDelegate.showToast(response.toString());
        }
    }
    
    @Override
    protected void onFailure(int requestId, Object response, String responseCode, String errmsg) {
        super.onFailure(requestId, response, responseCode, errmsg);
        if (requestId == R.id.demo_id) {
            viewDelegate.hideLoadView();
            viewDelegate.showToast(response.toString());
        }
    }
}
