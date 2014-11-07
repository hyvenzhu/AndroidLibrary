package com.android.baseline.test;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.parser.JsonParser;

public class UserLoginJsonParser extends JsonParser
{
    @Override
    public void parseResponse(final InfoResult infoResult, final JSONObject jsonObject)
    {
        JSONObject resultObj = jsonObject.optJSONObject("result");
        UserInfo userInfo = JSON.parseObject(resultObj.optJSONObject("userInfo").toString(), UserInfo.class);
        infoResult.setExtraObj(userInfo);
    }
}
