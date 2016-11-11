package com.android.baseline.framework.logic.net;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * SSL工具
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/8 15:46]
 */
public class SSLFactory {
    /**
     * 创建SSLSocketFactory
     *
     * @param context
     * @param inputStreams 包含公钥的cer证书
     * @return
     */
    public static SSLSocketFactory build(Context context, InputStream... inputStreams) {
        try {
            KeyStore keyStore = buildKeyStore(context, inputStreams);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyStoreException e1) {
        } catch (CertificateException e2) {
        } catch (NoSuchAlgorithmException e3) {
        } catch (IOException e4) {
        } catch (KeyManagementException e5) {
        }
        return null;
    }

    /**
     * 创建SSLSocketFactory(双向认证)
     *
     * @param context
     * @param bksRawResIds jks转化之后的bks格式证书(转化方式: https://sourceforge.net/projects/portecle/files/latest/download?source=files下载portecle-1.9.zip)
     * @param pwd 证书的秘钥
     * @param inputStreams 包含公钥的cer证书
     * @return
     */
    public static SSLSocketFactory buildWithClientAuth(Context context, int bksRawResIds, String pwd, InputStream... inputStreams) {
        try {
            KeyStore keyStore = buildKeyStore(context, inputStreams);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(buildClient(context, bksRawResIds, pwd), tmf.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyStoreException e1) {
        } catch (CertificateException e2) {
        } catch (NoSuchAlgorithmException e3) {
        } catch (IOException e4) {
        } catch (KeyManagementException e5) {
        }
        return null;
    }

    private static KeyManager[] buildClient(Context context, int bksRawResIds, String pwd)
    {
        try {
            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // jks证书需要转化为bks格式
            clientKeyStore.load(context.getResources().openRawResource(bksRawResIds), pwd.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, pwd.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建SSLSocketFactory(信任所有https)
     *
     * @return
     */
    @SuppressWarnings("强烈不建议使用, https形同虚设")
    public static SSLSocketFactory build() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException e1) {
        } catch (KeyManagementException e2) {
        }
        return sslContext.getSocketFactory();
    }

    /**
     * 创建KeyStore
     *
     * @param context
     * @param inputStreams
     * @return
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static KeyStore buildKeyStore(Context context, InputStream... inputStreams) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        List<Certificate> cas = readCert(context, inputStreams);
        for (int i = 0; cas != null && i < cas.size(); i++) {
            keyStore.setCertificateEntry(Integer.toString(i), cas.get(i));
        }
        return keyStore;
    }

    /**
     * 创建Certificate
     * @param context
     * @param inputStreams
     * @return
     */
    private static List<Certificate> readCert(Context context, InputStream... inputStreams) throws CertificateException, IOException {
        List<Certificate> cas = new ArrayList<>();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        for (InputStream is : inputStreams) {
            Certificate ca = cf.generateCertificate(is);
            is.close();
            cas.add(ca);
        }
        return cas;
    }

    /**
     * 信任所有https
     */
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
