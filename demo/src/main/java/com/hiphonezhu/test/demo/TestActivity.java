package com.hiphonezhu.test.demo;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.ui.BasicActivity;
import com.hiphonezhu.test.demo.base.ListEntry;

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
                moduleLogic.mobilenumber();
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
    protected boolean isToolBarVisible() {
        return super.isToolBarVisible();
    }

    @Override
    protected int getToolBarColor() {
        return super.getToolBarColor();
    }

    @Override
    public void onResponse(Message msg) {
        super.onResponse(msg);
        switch (msg.what)
        {
            case R.id.mobilenumber:
                hideProgress();
                if (checkResponse(msg))
                {
                    InfoResult<MobileBean> infoResult = (InfoResult)msg.obj;
                    MobileBean retData = infoResult.getExtraObj();
                    showToast(retData.toString());
                }
                break;
            case R.id.citylist:
                hideProgress();
                if (checkResponse(msg))
                {
                    InfoResult<ListEntry<MobileBean>> infoResult = (InfoResult)msg.obj;
                    ListEntry<MobileBean> retData = infoResult.getExtraObj();
                    showToast(retData.toString());
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
