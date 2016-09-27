package com.android.baseline.framework.logic.net;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitManager
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/27 17:15]
 */

public class RetrofitManager {
    Map<String, Retrofit> retrofitPool = new HashMap<>();

    OkHttpClient client;

    static RetrofitManager sInstance;
    private RetrofitManager()
    {
        client = getClient();
    }

    public static RetrofitManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (RetrofitManager.class)
            {
                if (sInstance == null)
                {
                    sInstance = new RetrofitManager();
                }
            }
        }
        return sInstance;
    }

    public Retrofit getRetrofit(String baseUrl)
    {
        Retrofit retrofit = retrofitPool.get(baseUrl);
        if (retrofitPool.get(baseUrl) == null)
        {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            retrofit = builder.build();
            retrofitPool.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    /**
     * custom OkHttpClient
     * @return
     */
    private OkHttpClient getClient()
    {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // log interceptor
                .retryOnConnectionFailure(true) // retry when connect failure
                .connectTimeout(10, TimeUnit.SECONDS) // connect timeout 10s
                .readTimeout(20, TimeUnit.SECONDS) // read timeout 20s
                .writeTimeout(20, TimeUnit.SECONDS); // write timeout 20s
        return builder.build();
    }
}
