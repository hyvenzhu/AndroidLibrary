package library.common.framework.logic.net;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import library.common.App;
import library.common.util.APKUtils;
import library.common.util.LogUtils;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit Manager
 * <p>
 * 支持Https, 需要使用之前初始化(在App初始化的地方设置, 例如Application#onCreate方法中):
 * RetrofitManager.getInstance().initCertificates(InputStream... cers);
 * 后续设置无效
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */

public class RetrofitManager {
    /**
     * Retrofit cache pool, key is 'baseUrl'
     */
    Map<String, Retrofit> retrofitPool = new HashMap<>();

    /**
     * single instance
     */
    static RetrofitManager sInstance;

    /**
     * default client
     */
    OkHttpClient client;

    List<Interceptor> networkInterceptorList;

    List<Interceptor> applicationInterceptorList;

    X509TrustManager trustManager;
    SSLSocketFactory sslFactory;

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

    public OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = buildClient();
        }
        return client;
    }

    /**
     * Return Retrofit by baseUrl
     *
     * @param baseUrl
     * @return
     */
    public synchronized Retrofit getRetrofit(String baseUrl) {
        if (client == null) {
            client = buildClient();
        }
        Retrofit retrofit = retrofitPool.get(baseUrl);
        if (retrofitPool.get(baseUrl) == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofit = builder.build();
            retrofitPool.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    /**
     * 设置拦截器
     * [需要在Application onCreate中初始化]
     *
     * @param applicationInterceptorList
     * @param networkInterceptorList
     */
    public void initInterceptor(List<Interceptor> applicationInterceptorList, List<Interceptor> networkInterceptorList) {
        this.applicationInterceptorList = applicationInterceptorList;
        this.networkInterceptorList = networkInterceptorList;
    }

    /**
     * 初始化 https
     * [需要在Application onCreate中初始化]
     */
    public void initHttps() {
        try {
            trustManager = SSLUtils.getDefaultX509TrustManager();
            sslFactory = SSLUtils.build(trustManager);

            client = buildClient();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Https证书
     * [需要在Application onCreate中初始化]
     *
     * @param cers 包含公钥的cer证书
     */
    public void initHttps(InputStream... cers) {
        try {
            trustManager = SSLUtils.getX509TrustManager(cers);
            sslFactory = SSLUtils.build(trustManager);

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
    public void initHttps(InputStream bks, String pwd, InputStream... cers) {
        try {
            trustManager = SSLUtils.getX509TrustManager(cers);
            sslFactory = SSLUtils.build(bks, pwd, trustManager);

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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (!TextUtils.isEmpty(message)
                        && message.startsWith("{")
                        && message.endsWith("}")) {
                    LogUtils.json(message);
                } else if (!TextUtils.isEmpty(message)) {
                    LogUtils.d(message);
                }
            }
        });
        if (APKUtils.isDebug()) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        File cacheDir = null;
        try {
            cacheDir = APKUtils.getDiskCacheDir(App.getInstance().getApplicationContext(), "Retrofit-Cache");
        } catch (Exception ex) {
            LogUtils.e(ex, null, (Object) null);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                // log interceptor
                .addInterceptor(loggingInterceptor)
                // retry when connect failure
                .retryOnConnectionFailure(true)
                // connect timeout 10s
                .connectTimeout(10, TimeUnit.SECONDS)
                // read timeout 20s
                .readTimeout(20, TimeUnit.SECONDS)
                // write timeout 20s
                .writeTimeout(20, TimeUnit.SECONDS)
                // Besides cache setting, we also need cache support(it usually controlled by server),
                // but, it can also be controlled by client with http header(just like "Cache-Control:public,max-age=120").
                .cache(cacheDir != null ? new Cache(cacheDir, 10 * 1024 * 1024) : null);
        if (!APKUtils.isDebug()) {
            // no proxy
            builder.proxy(Proxy.NO_PROXY);
        }
        if (sslFactory != null) {
            builder.sslSocketFactory(sslFactory, trustManager);
        }
        if (networkInterceptorList != null && networkInterceptorList.size() > 0) {
            for (Interceptor interceptor : networkInterceptorList) {
                builder.addNetworkInterceptor(interceptor);
            }
        }
        builder.addInterceptor(new ProgressResponseInterceptor());
        if (applicationInterceptorList != null && applicationInterceptorList.size() > 0) {
            for (Interceptor interceptor : applicationInterceptorList) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }
}
