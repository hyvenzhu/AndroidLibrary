package com.hiphonezhu.test.demo.base.convertor;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.android.baseline.framework.logic.InfoResult;
import com.hiphonezhu.test.demo.base.BaseJsonParser;
import com.hiphonezhu.test.demo.base.ListEntry;

import java.lang.reflect.Type;

/**
 * 返回结果解析器-业务数据为Array
 * @see ObjectResponseConvertor
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/06/24 10:23]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class ArrayResponseConvertor extends BaseJsonParser {
    private Class type;
    private Class arrayType;

    /**
     * 默认为ListEntry, type类型即为ListEntry的T
     * @param type
     */
    public ArrayResponseConvertor(Class type)
    {
        this.type = type;
        this.arrayType = ListEntry.class;
    }

    /**
     * 使用自定义的arrayType, type类型即为arrayType的T
     * @param type
     * @param arrayType
     */
    public ArrayResponseConvertor(Class type, Class arrayType)
    {
        this.type = type;
        this.arrayType = arrayType;
    }

    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject dataObject) {
        if (infoResult.isSuccess() && dataObject != null && !TextUtils.isEmpty(dataObject.toJSONString())) {
            infoResult.setExtraObj(JSON.parseObject(dataObject.toJSONString(),
                    new ParameterizedTypeImpl(new Type[] {type}, null, arrayType)));
        }
    }
}
