package com.jcgroup.demo.ui;

import android.view.View;

import com.jcgroup.demo.R;
import com.jcgroup.demo.base.BaseActivity;
import com.jcgroup.demo.logic.ModuleALogic;
import com.jcgroup.demo.ui.view.MainDelegate;


/**
 * @author zhuhf
 * @version [DX-AndroidLibrary, 2018-03-07]
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
