package com.hiphonezhu.test.demo;

import com.android.baseline.framework.asyncquery.TaskExecutor;
import com.android.baseline.framework.logic.BaseLogic;
import com.android.baseline.framework.logic.SimpleLogic;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块的接口等
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:02]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class ModuleLogic extends SimpleLogic {
    public ModuleLogic(Object subscriber) {
        super(subscriber);
    }

    public void testNet()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "15210011578");
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey", "805ba6b0b186fe263c77d4e352d1e605");
        sendGetRequest(R.id.testNet, "http://apis.baidu.com/apistore/mobilenumber/mobilenumber", params, new NetParser(), headers);
    }

    public void testTask()
    {
        TaskExecutor.getInstance().execute(new ModuleTask(R.id.testTask, this));
    }
}
