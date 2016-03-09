package com.hiphonezhu.test.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.parser.JsonParser;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:24]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public abstract class BaseJsonParser extends JsonParser {
    @Override
    public InfoResult doParse(String response) throws JSONException {
        JSONObject jsonObject = JSON.parseObject(response);
        InfoResult infoResult = new InfoResult.Builder()
                .success(jsonObject.getIntValue("errNum") == 0)
                .errorCode(jsonObject.getString("errNum"))
                .desc(jsonObject.getString("retMsg"))
                .build();
        parseResponse(infoResult, jsonObject);
        return infoResult;
    }

    @Override
    protected abstract void parseResponse(InfoResult infoResult, JSONObject jsonObject);
}
