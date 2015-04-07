package com.android.baseline.test;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.android.baseline.R;
import com.android.baseline.framework.asyncquery.TaskExecutor;
import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.ui.BasicActivity;
import com.android.baseline.framework.ui.base.annotations.ViewInject;
/**
 * 演示如何使用框架
 * [
 *   1、View注解和事件绑定
 *   2、网络模块
 *   3、耗时任务执行模块
 *   4、日志打印Logger
 *   5、通用数据加载控件LoadingView
 * ]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-19]
 */
public class TestActivity extends BasicActivity
{
    TestLogic logic = null;
    @ViewInject(value=R.id.result_txt)
    private TextView resultTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        logic = new TestLogic(this);
    }
    
    @Override
    public void onResponse(Message msg)
    {
        super.onResponse(msg);
        switch (msg.what)
        {
            case R.id.onLoading:
                resultTxt.setText("");
                // 网络请求
//                logic.userLogin();
                
                // 非网络请求, 耗时任务
                TaskExecutor.getInstance().execute(new TestTask(R.id.testTask, this));
                break;
            case R.id.testHttp:
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    // 业务逻辑成功
                    resultTxt.setText(infoResult.getExtraObj().toString());
                    onSuccess();
                }
                else
                {
                    onFailure();
                }
                break;
            case R.id.testTask:
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    // 业务逻辑成功
                    resultTxt.setText(infoResult.getExtraObj().toString());
                    onSuccess();
                }
                else
                {
                    onFailure();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterAll(logic);
        Logger.d("TestActivity", "onDestroy");
    }
}
