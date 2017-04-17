package com.android.baseline.framework.logic.net;

import com.android.baseline.AppDroid;
import com.android.baseline.BuildConfig;
import com.android.baseline.util.APKUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit Manager
 * <p>
 * 支持Https, 需要使用之前初始化(在App初始化的地方设置, 例如Application#onCreate方法中):
 *      RetrofitManager.getInstance().initCertificates(InputStream... cers);
 * 后续设置无效
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/27 17:15]
 */

public class RetrofitManager {
    Map<String, Retrofit> retrofitPool = new HashMap<>(); // Retrofit cache pool, key is 'baseUrl'

    static RetrofitManager sInstance; // single instance

    OkHttpClient client; // default client

    Interceptor networkInterceptor;

    /**
     * Private constructor
     */
    private RetrofitManager() {
    }

    /**
     * Single instance
     *
     * @return
     */
    public static RetrofitManager getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitManager.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * Return Retrofit by baseUrl
     *
     * @param baseUrl
     * @return
     */
    public synchronized Retrofit getRetrofit(String baseUrl, Interceptor networkInterceptor) {
        this.networkInterceptor = networkInterceptor;
        if (client == null) {
            client = buildClient();
        }

        Retrofit retrofit = retrofitPool.get(baseUrl);
        if (retrofitPool.get(baseUrl) == null) {
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

    X509TrustManager trustManager;
    SSLSocketFactory sslFactory;

    /**
     * 设置Https证书
     * [需要在Application onCreate中初始化]
     *
     * @param cers 包含公钥的cer证书
     */
    public void initCertificates(InputStream... cers) {
        try {
            trustManager = SSLFactory.getX509TrustManager(cers);
            sslFactory = SSLFactory.build(trustManager);

            client = buildClient();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Https证书(双向认证)
     * [需要在Application onCreate中初始化]
     *
     * @param bks  jks转化之后的bks格式证书(转化方式: https://sourceforge.net/projects/portecle/files/latest/download?source=files下载portecle-1.9.zip)
     * @param pwd  证书的秘钥
     * @param cers 包含公钥的cer证书
     */
    public void initCertificates(InputStream bks, String pwd, InputStream... cers) {
        try {
            trustManager = SSLFactory.getX509TrustManager(cers);
            sslFactory = SSLFactory.build(bks, pwd, trustManager);

            client = buildClient();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom OkHttpClient
     *
     * @return
     */
    private OkHttpClient buildClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // log interceptor
                .retryOnConnectionFailure(true) // retry when connect failure
                .connectTimeout(10, TimeUnit.SECONDS) // connect timeout 10s
                .readTimeout(20, TimeUnit.SECONDS) // read timeout 20s
                .writeTimeout(20, TimeUnit.SECONDS) // write timeout 20s
                // Besides cache setting, we also need cache support(it usually controlled by server),
                // but, it can also be controlled by client with http header(just like "Cache-Control:public,max-age=120").
                .cache(new Cache(APKUtil.getDiskCacheDir(AppDroid.getInstance().getApplicationContext(), "Retrofit-Cache"), 10 * 1024 * 1024));
        if (sslFactory != null) {
            builder.sslSocketFactory(sslFactory, trustManager);
        }
        if (networkInterceptor != null) {
            builder.addNetworkInterceptor(networkInterceptor);
        }
        return builder.build();
    }
}
