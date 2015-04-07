package com.android.baseline.test;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.baseline.R;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.ui.BasicFragment;
import com.android.baseline.framework.ui.base.annotations.ViewInject;
import com.android.baseline.framework.ui.base.annotations.event.OnClick;

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
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    // 业务逻辑成功
                    resultTxt.setText(infoResult.getExtraObj().toString());
                }
                break;
            case R.id.testTask:
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    // 业务逻辑成功
                    resultTxt.setText(infoResult.getExtraObj().toString());
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
        unregisterAll(logic);
    }
}
