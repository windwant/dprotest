package org.rmi.common;

import com.sun.net.httpserver.HttpHandler;
import org.springframework.remoting.support.SimpleHttpServerFactoryBean;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 18-5-18.
 */
public class SimpleHttpServerUtil {

    private SimpleHttpServerUtil(){}

    /**
     * SimpleHttpServer 简单内置http发布服务
     * @param url
     * @param exporter
     */
    public static void startSimpleHttpServer(String url, final HttpHandler exporter){
        SimpleHttpServerFactoryBean http = new SimpleHttpServerFactoryBean();
        http.setPort(DConstants.HTTP_INVOKER_PORT);
        final String finalUrl = url;
        http.setContexts(new HashMap<String, HttpHandler>() {{
            put(finalUrl, exporter);
        }});
        try {
            http.afterPropertiesSet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
