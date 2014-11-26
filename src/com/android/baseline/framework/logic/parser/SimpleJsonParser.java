package com.android.baseline.framework.logic.parser;

import org.json.JSONObject;

import com.android.baseline.framework.logic.InfoResult;
/**
 * 通用操作解析器
 * @author hiphonezhu@gmail.com
 * @version [OApp, 2014-11-10]
 */
public class SimpleJsonParser extends JsonParser
{
    /** 需要回传的值*/
    private Object extraObj;
    public SimpleJsonParser(Object extraObj)
    {
        this.extraObj = extraObj;
    }
    
    @Override
    public void parseResponse(InfoResult infoResult, JSONObject jsonObject)
    {
        infoResult.setExtraObj(extraObj);
    }
}
