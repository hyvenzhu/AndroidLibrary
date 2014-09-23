package com.android.baseline.test;

import org.json.JSONObject;

import com.android.baseline.R;
import com.android.baseline.framework.log.Logger;
import com.android.baseline.framework.logic.BaseLogic;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.volley.InfoResultRequest;
import com.android.baseline.framework.volley.InfoResultRequest.ResponseParserListener;

public class TestLogic extends BaseLogic
{
    public void userLogin()
    {
        InfoResultRequest infoResultRequest = new InfoResultRequest(R.id.testHttp, "http://121.40.140.54:8080/ShiCC/userLogin?name=jack&mobile=18652048507", 
                new ResponseParserListener()
                {
                    @Override
                    public InfoResult doParse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            // 解析业务状态
                            InfoResult infoResult = parseLogicSatus(jsonObject);
                            // string 2 bean封装
                            JSONObject resultObj = jsonObject.optJSONObject("result");
                            infoResult.setExtraObj(resultObj);
                            return infoResult;
                        }
                        catch (Exception e)
                        {
                            Logger.e("TestLogic", e);
                            return null;
                        }
                    }
                }, this);
        // 可以不设置, 取消请求用
        infoResultRequest.setTag(R.id.testHttp);
        requestQueue.add(infoResultRequest);
    }
}
