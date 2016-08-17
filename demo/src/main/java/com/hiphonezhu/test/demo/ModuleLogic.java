package com.hiphonezhu.test.demo;

import com.android.baseline.framework.logic.SimpleLogic;
import com.hiphonezhu.test.demo.base.convertor.ArrayResponseConvertor;
import com.hiphonezhu.test.demo.base.convertor.ObjectResponseConvertor;

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

    public void mobilenumber()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "15210011578");
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey", "805ba6b0b186fe263c77d4e352d1e605");
        sendGetRequest(R.id.mobilenumber, "http://apis.baidu.com/apistore/mobilenumber/mobilenumber", params, new ObjectResponseConvertor(MobileBean.class), headers);
    }

    public void list()
    {
        String response = "{\n" +
                "    \"errNum\": 0,\n" +
                "    \"retMsg\": \"success\",\n" +
                "    \"retData\": {\n" +
                "        \"rows\": [\n" +
                "            {\n" +
                "                \"phone\": \"15210011578\",\n" +
                "                \"prefix\": \"1521001\",\n" +
                "                \"supplier\": \"79fb52a8\",\n" +
                "                \"province\": \"-\",\n" +
                "                \"city\": \"-\",\n" +
                "                \"suit\": \"1525361\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        // 返回结果json数据格式为response
        sendGetRequest(R.id.citylist, "http://xxxx", new ArrayResponseConvertor(MobileBean.class));
    }
}
