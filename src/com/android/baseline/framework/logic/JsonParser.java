package com.android.baseline.framework.logic;

import org.json.JSONException;
import org.json.JSONObject;

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
     * @param logicStatus
     * @return InfoResult
     * @throws JSONException 
     */
    protected InfoResult parseLogicSatus(String logicStatus) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(logicStatus);
        return new InfoResult.Builder()
                   .success(jsonObject.optBoolean("success"))
                   .errorCode(jsonObject.optString("errorCode"))
                   .desc(jsonObject.optString("desc"))
                   .build();
    }
}
