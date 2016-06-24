package com.hiphonezhu.test.demo.base.convertor;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.hiphonezhu.test.demo.base.BaseJsonParser;

/**
 * 返回结果解析器-业务数据为Object
 * @see ArrayResponseConvertor
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/06/24 10:23]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class ObjectResponseConvertor extends BaseJsonParser {
    private Class cls;

    public ObjectResponseConvertor(Class cls)
    {
        this.cls = cls;
    }

    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject dataObject) {
        if (infoResult.isSuccess() && dataObject != null && !TextUtils.isEmpty(dataObject.toJSONString())) {
            infoResult.setExtraObj(JSON.parseObject(dataObject.toJSONString(), cls));
        }
    }
}
