package com.jsp.updaterk312;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class CustomerHttpClient {
    private static HttpClient customerHttpClient;

    public static synchronized HttpClient getHttpClient() {
        HttpClient httpClient;
        synchronized (CustomerHttpClient.class) {
            if (customerHttpClient == null) {
                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, "UTF-8");
                HttpProtocolParams.setUseExpectContinue(params, true);
                HttpProtocolParams.setUserAgent(params, "rk29sdk/4.0");
                ConnManagerParams.setMaxTotalConnections(params, 100);
                ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
                connPerRoute.setMaxForRoute(new HttpRoute(new HttpHost("locahost", 80)), 50);
                ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
                ConnManagerParams.setTimeout(params, 1000);
                HttpConnectionParams.setConnectionTimeout(params, 2000);
                HttpConnectionParams.setSoTimeout(params, 4000);
                HttpClientParams.setRedirecting(params, true);
                SchemeRegistry schReg = new SchemeRegistry();
                schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                customerHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
            }
            httpClient = customerHttpClient;
        }
        return httpClient;
    }
}
