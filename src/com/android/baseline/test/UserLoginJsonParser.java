package com.android.baseline.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.parser.JsonParser;

public class UserLoginJsonParser extends JsonParser
{
    @Override
    public void parseResponse(final InfoResult infoResult, final JSONObject jsonObject)
    {
        JSONObject resultObj = jsonObject.getJSONObject("result").getJSONObject("userInfo");
        UserInfo userInfo = JSON.parseObject(resultObj.toJSONString(), UserInfo.class);
        infoResult.setExtraObj(userInfo);
    }
}
