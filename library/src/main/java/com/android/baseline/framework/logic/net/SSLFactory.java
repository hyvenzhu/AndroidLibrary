package com.android.baseline.framework.logic.net;

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
import java.util.Arrays;
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
     * @param trustManager
     * @return
     */
    public static SSLSocketFactory build(X509TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e3) {
        } catch (KeyManagementException e5) {
        }
        return null;
    }

    /**
     * 创建SSLSocketFactory(双向认证)
     *
     * @param bks          jks转化之后的bks格式证书(转化方式: https://sourceforge.net/projects/portecle/files/latest/download?source=files下载portecle-1.9.zip)
     * @param pwd          证书的秘钥
     * @param trustManager
     * @return
     */
    public static SSLSocketFactory build(InputStream bks, String pwd, TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(buildClient(bks, pwd), new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e3) {
        } catch (KeyManagementException e5) {
        }
        return null;
    }

    /**
     * 创建X509TrustManager
     *
     * @param inputStreams 包含公钥的cer证书
     * @return
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     */
    public static X509TrustManager getX509TrustManager(InputStream... inputStreams) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore keyStore = buildKeyStore(inputStreams);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private static KeyManager[] buildClient(InputStream bks, String pwd) {
        try {
            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // jks证书需要转化为bks格式
            clientKeyStore.load(bks, pwd.toCharArray());
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
     * @param inputStreams
     * @return
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static KeyStore buildKeyStore(InputStream... inputStreams) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        List<Certificate> cas = readCert(inputStreams);
        for (int i = 0; cas != null && i < cas.size(); i++) {
            keyStore.setCertificateEntry(Integer.toString(i), cas.get(i));
        }
        return keyStore;
    }

    /**
     * 创建Certificate
     *
     * @param inputStreams
     * @return
     */
    private static List<Certificate> readCert(InputStream... inputStreams) throws CertificateException, IOException {
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
