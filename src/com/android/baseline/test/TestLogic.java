package com.android.baseline.test;

import com.android.baseline.R;
import com.android.baseline.framework.logic.BaseLogic;
import com.android.baseline.framework.volley.InfoResultRequest;

public class TestLogic extends BaseLogic
{
    public void userLogin()
    {
        // json
        InfoResultRequest infoResultRequest = new InfoResultRequest(R.id.testHttp, 
                "http://121.40.140.54:8080/ShiCC/userLogin?name=jack&mobile=18652048507", new UserLoginJsonParser(), this);
        // xml
//        InfoResultRequest infoResultRequest = new InfoResultRequest(R.id.testHttp, 
//                "http://121.40.140.54:8080/ShiCC/userLogin?name=jack&mobile=18652048507", new UserLoginXmlParser(), this);
        // 第二个参数可以不设置, 取消请求用
        sendRequest(infoResultRequest, R.id.testHttp);
    }
}
