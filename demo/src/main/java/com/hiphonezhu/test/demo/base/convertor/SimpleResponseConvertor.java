package com.hiphonezhu.test.demo.base.convertor;

import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.hiphonezhu.test.demo.base.BaseJsonParser;

/**
 * 返回结果解析器
 * 忽略业务数据，只关心成功与失败
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/06/24 10:23]
 */
public class SimpleResponseConvertor extends BaseJsonParser {

    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject dataObject) {
        // do nothing
    }
}
