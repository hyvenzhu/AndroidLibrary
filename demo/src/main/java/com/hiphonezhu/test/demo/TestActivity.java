package com.hiphonezhu.test.demo;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.ui.BasicActivity;

/**
 *  网络请求测试
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class TestActivity extends BasicActivity{
    private ModuleLogic moduleLogic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        moduleLogic = new ModuleLogic(this);

        // 网络请求
        findViewById(R.id.net_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.testNet();
            }
        });

        // 本地任务
        findViewById(R.id.task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.testTask();
            }
        });
    }

    @Override
    public void onResponse(Message msg) {
        super.onResponse(msg);
        switch (msg.what)
        {
            case R.id.testNet:
                hideProgress();
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    JSONObject retData = (JSONObject)infoResult.getExtraObj();
                    showToast(retData.toJSONString());
                }
                break;
            case R.id.testTask:
                hideProgress();
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    showToast(infoResult.getExtraObj().toString());
                }
                break;
        }
    }
}
