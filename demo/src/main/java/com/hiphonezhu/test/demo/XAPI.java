package com.hiphonezhu.test.demo;

import com.android.baseline.framework.logic.InfoResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @Streaming // 输出InputStream, 没有将body在内存中转为字节(否则, 会OOM)
    @GET
    Observable<ResponseBody> download(@Url String downloadUrl);

    @POST
    @Multipart
    Observable<UploadResult> upload(@Url String url, @Part("account") String account, @Part MultipartBody.Part file);

    // 同upload, 只不过filename无法修改
    @POST
    @Multipart
    Observable<UploadResult> upload2(@Url String url, @Part("account") String account, @Part("avatar\"; filename=\"file1.jpeg") RequestBody body);

    @POST
    @Multipart
    Observable<UploadResult> batchUpload(@Url String url, @PartMap Map<String, RequestBody> params);
}
