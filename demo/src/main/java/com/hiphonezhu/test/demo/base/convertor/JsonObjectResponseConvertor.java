package com.hiphonezhu.test.demo.base.convertor;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.hiphonezhu.test.demo.base.BaseJsonParser;

/**
 * 返回结果解析器-业务数据为Object（但不希望去做Bean转换, 这种情况直接返回JsonObject）
 * @see ArrayResponseConvertor
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/06/24 10:23]
 */
public class JsonObjectResponseConvertor extends BaseJsonParser {
    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject dataObject) {
        if (infoResult.isSuccess() && dataObject != null && !TextUtils.isEmpty(dataObject.toJSONString())) {
            infoResult.setExtraObj(dataObject);
        }
    }
}
