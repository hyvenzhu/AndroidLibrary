package com.android.baseline.framework.logic.net;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
     * @param certRawResIds ca证书raw资源数组
     * @return
     */
    public static SSLSocketFactory build(Context context, int... certRawResIds) {
        try {
            KeyStore keyStore = buildKeyStore(context, certRawResIds);

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
     * 创建SSLSocketFactory(信任所有https)
     *
     * @return
     */
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
     * @param certRawResIds
     * @return
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static KeyStore buildKeyStore(Context context, int... certRawResIds) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        List<Certificate> cas = readCert(context, certRawResIds);
        for (int i = 0; cas != null && i < cas.size(); i++) {
            keyStore.setCertificateEntry(Integer.toString(i), cas.get(i));
        }
        return keyStore;
    }

    /**
     * 创建Certificate
     * @param context
     * @param certRawResIds
     * @return
     */
    private static List<Certificate> readCert(Context context, int... certRawResIds) throws CertificateException {
        List<Certificate> cas = new ArrayList<>();
        for (int certRawResId : certRawResIds) {
            InputStream inputStream = context.getResources().openRawResource(certRawResId);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(inputStream);
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
