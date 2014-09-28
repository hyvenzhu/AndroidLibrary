package com.android.baseline.test;

import org.json.JSONObject;

import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.parser.JsonParser;

public class UserLoginJsonParser extends JsonParser
{
    @Override
    public void parseResponse(final InfoResult infoResult, final JSONObject jsonObject)
    {
        JSONObject resultObj = jsonObject.optJSONObject("result");
        infoResult.setExtraObj(resultObj);
    }
}
