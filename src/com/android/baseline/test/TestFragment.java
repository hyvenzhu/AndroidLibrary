package com.android.baseline.test;

import com.android.baseline.R;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.ui.BasicFragment;
import com.android.baseline.framework.ui.base.annotations.ViewInject;
import com.android.baseline.framework.ui.base.annotations.event.OnClick;
import com.android.volley.VolleyError;

public class TestFragment extends BasicFragment
{
    TestLogic logic = null;
    @ViewInject(value=R.id.test_btn)
    private Button testBtn;
    @ViewInject(value=R.id.result_txt)
    private TextView resultTxt;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View v = inflate(inflater, container, R.layout.fragment_test, this);
        logic = new TestLogic(this);
        return v;
    }
    
    @OnClick({R.id.test_btn})
    public void userLogin(View v)
    {
        switch (v.getId())
        {
            case R.id.test_btn:
                showProgress("请稍后...");
                resultTxt.setText("");
                // 网络请求
                logic.userLogin();
                // 取消请求
//                logic.cancelAll(R.id.testHttp);
                
                // 非网络请求, 耗时任务
//                TaskExecutor.getInstance().execute(new TestTask(R.id.testTask));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(Message msg)
    {
        super.onResponse(msg);
        switch (msg.what)
        {
            case R.id.testHttp:
                if (msg.obj instanceof InfoResult)
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    if (infoResult.isSuccess())
                    {
                        // 业务逻辑成功
                        resultTxt.setText(infoResult.getExtraObj().toString());
                    }
                    else if ("具体错误码".equals(infoResult.getErrorCode()))
                    {
                        Logger.w("TestActivity", "error code >>> " + infoResult.getErrorCode());
                    }
                }
                else if (msg.obj instanceof VolleyError)
                {
                    // 可提示网络错误，具体类型有TimeoutError ServerError
                    Logger.w("TestActivity", (VolleyError)msg.obj);
                }
                break;
            case R.id.testTask:
                if (msg.obj instanceof InfoResult)
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    if (infoResult.isSuccess())
                    {
                        // 业务逻辑成功
                        resultTxt.setText(infoResult.getExtraObj().toString());
                    }
                    else if ("具体错误码".equals(infoResult.getErrorCode()))
                    {
                        Logger.w("TestActivity", "error code >>> " + infoResult.getErrorCode());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        logic.unregisterAll();
    }
}
