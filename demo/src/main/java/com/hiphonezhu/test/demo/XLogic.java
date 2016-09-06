package com.hiphonezhu.test.demo;

import com.android.baseline.framework.logic.BaseLogic;

/**
 * 业务模块
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/3 12:12]
 */
public class XLogic extends BaseLogic {
    PhoneAPI phoneService;
    public XLogic(Object subscriber) {
        super(subscriber);
        phoneService = create(PhoneAPI.class);
    }

    public void getResult(String phone)
    {
        sendRequest(phoneService.getResult(phone, "8e13586b86e4b7f3758ba3bd6c9c9135"), R.id.mobilenumber);
    }

    @Override
    public String getBaseUrl() {
        return "http://apis.baidu.com/";
    }
}
