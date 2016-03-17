package com.hiphonezhu.test.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.baseline.framework.logic.InfoResult;
import com.hiphonezhu.test.demo.base.BaseJsonParser;

/**
 * [description about this class]
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:26]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class NetParser extends BaseJsonParser {
    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject jsonObject) {
        if (infoResult.isSuccess())
        {
            JSONObject retData = jsonObject.getJSONObject("retData");
            // json到bean等处理可以在这里做
            MobileBean bean = JSON.parseObject(retData.toJSONString(), MobileBean.class);
            infoResult.setExtraObj(bean);
        }
    }
}
