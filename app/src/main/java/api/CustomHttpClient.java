package api;

import android.content.Context;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by cognoscis on 8/3/18.
 */

public class CustomHttpClient {

    public static String[] userAgentList = {
            "ro.build.version.release",
            "ro.product.model",
            "ro.cvte.ota.version",
            "ro.cloudwalker.cota.version"
    };
    static int TIMEOUT = 30;

    public static boolean isUrlHTTPS(String url) {
        return URLUtil.isHttpsUrl(url);
    }


    public static OkHttpClient getOkHttps(Context context) {
        SSLContext sslContext;
        TrustManager[] trustManagers;
        try {
            trustManagers = getTrustManagers(context);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]);


            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    boolean verified = hv.verify("*.cloudwalker.tv", sslSession);
                    return true;
                }
            });


            //adding logging if in DEBUG MODE
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            return builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static OkHttpClient getOkHttp(Context context) {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            return builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static OkHttpClient getHttpClient(Context context, String url) {
        if (isUrlHTTPS(url)) {
            return getOkHttps(context);
        } else {
            return getOkHttp(context);
        }
    }

    private static TrustManager[] getTrustManagers(Context context) {
        TrustManager[] trustManagers;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            InputStream certInputStream = context.getAssets().open("dev-server.crt");
            BufferedInputStream bis = new BufferedInputStream(certInputStream);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = certificateFactory.generateCertificate(bis);
                keyStore.setCertificateEntry("ca", cert);
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
