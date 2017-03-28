package com.ifma.appmhelp.lib;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by leo on 3/27/17.
 */

public class ClientHTTP {

    final static int proxyPort = 8080;
    final static String proxyHost = "192.168.0.8";
    final static String username = "leonardo.pinheiro";
    final static String password = "940530";

    private static Authenticator proxyAuthenticator = new Authenticator() {
        public Request authenticate(Route route, Response response) throws IOException {
            String credential = Credentials.basic(username, password);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        }
    };

    public static OkHttpClient getHTTPClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                //.proxyAuthenticator(proxyAuthenticator)
                .build();
    }
}
