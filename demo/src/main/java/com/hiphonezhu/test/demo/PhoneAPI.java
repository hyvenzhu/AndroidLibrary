package com.hiphonezhu.test.demo;

import com.android.baseline.framework.logic.InfoResult;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * API接口定义
 * @author hiphonezhu@gmail.com
 * @version [RetrofitDemo, 16/8/18 10:37]
 */
public interface PhoneAPI {
    @GET("apistore/mobilenumber/mobilenumber")
    Observable<InfoResult<MobileBean>> getResult(@Query("phone") String phone, @Header("apiKey") String apiKey);
}
