package com.hiphonezhu.test.demo;

import com.android.baseline.framework.logic.InfoResult;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * API接口定义
 * @author hiphonezhu@gmail.com
 * @version [RetrofitDemo, 16/8/18 10:37]
 */
public interface XAPI {
    @GET("apistore/mobilenumber/mobilenumber")
    Observable<InfoResult<MobileBean>> getResult(@Query("phone") String phone, @Header("apiKey") String apiKey);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String downloadUrl);
}
