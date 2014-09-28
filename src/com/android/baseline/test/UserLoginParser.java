package com.android.baseline.test;

import org.json.JSONObject;

import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.JsonParser;

public class UserLoginParser extends JsonParser
{
    @Override
    public InfoResult doParse(String response)
    {
        try
        {
            // 解析业务状态
            InfoResult infoResult = parseLogicSatus(response);
            // string 2 bean封装
            JSONObject jsonObject = new JSONObject(response);
            JSONObject resultObj = jsonObject.optJSONObject("result");
            infoResult.setExtraObj(resultObj);
            return infoResult;
        }
        catch (Exception e)
        {
            Logger.e("TestLogic", e);
            return null;
        }
    }

}
