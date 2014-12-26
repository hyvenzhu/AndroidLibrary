package com.android.baseline.framework.logic.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.volley.InfoResultRequest.ResponseParserListener;
/**
 * A abstract class that parse logic status with type json
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-28]
 */
public abstract class JsonParser implements ResponseParserListener
{
    /**
     * 解析服务器结果的状态信息(业务成功与失败, 对应错误码和描述信息等)
     * @param response
     * @return InfoResult
     * @throws JSONException, Exception
     */
    public InfoResult doParse(final String response) throws JSONException
    {
        JSONObject jsonObject = JSON.parseObject(response);
        InfoResult infoResult = new InfoResult.Builder()
                   .success(jsonObject.getBooleanValue("success"))
                   .errorCode(jsonObject.getString("errorCode"))
                   .desc(jsonObject.getString("desc"))
                   .build();
        parseResponse(infoResult, jsonObject);
        return infoResult;
    }
    
    /**
     * 解析业务数据
     * @param infoResult
     * @param jsonObject
     */
    public abstract void parseResponse(final InfoResult infoResult, final JSONObject jsonObject);
}
