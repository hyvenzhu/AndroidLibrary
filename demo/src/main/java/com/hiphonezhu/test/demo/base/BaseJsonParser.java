package com.hiphonezhu.test.demo.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.parser.JsonParser;

/**
 * 返回结果通用信息解析, 一个标准的格式如下
 数组：
 {
    "errNum": 0,
    "retMsg": "success",
    "retData": {
        "count": 100,
        "rows": [
            {
                "phone": "15210011578",
                "prefix": "1521001",
                "supplier": "79fb52a8",
                "province": "-",
                "city": "-",
                "suit": "1525361"
            }
        ]
    }
 }
 对象：
 {
    "errNum": 0,
    "retMsg": "success",
    "retData": {
        "phone": "15210011578",
        "prefix": "1521001",
        "supplier": "79fb52a8",
        "province": "-",
        "city": "-",
        "suit": "1525361"
    }
 }
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:24]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public abstract class BaseJsonParser extends JsonParser {
    @Override
    public InfoResult doParse(String response) throws JSONException {
        JSONObject jsonObject = JSON.parseObject(response);
        InfoResult infoResult = new InfoResult(jsonObject.getIntValue("errNum") == 0,
                jsonObject.getString("errNum"),
                jsonObject.getString("retMsg"));
        parseResponse(infoResult, jsonObject.getJSONObject("retData"));
        return infoResult;
    }

    @Override
    protected abstract void parseResponse(InfoResult infoResult, JSONObject dataObject);
}
