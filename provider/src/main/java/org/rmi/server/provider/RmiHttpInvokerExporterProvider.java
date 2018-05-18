package org.rmi.server.provider;

import com.sun.net.httpserver.HttpHandler;
import org.rmi.common.DConstants;
import org.rmi.server.RMIProvider;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter;
import org.springframework.remoting.support.SimpleHttpServerFactoryBean;
import java.rmi.Remote;
import java.util.HashMap;

/**
 * SimpleHttpInvokerServiceExporter
 * SimpleHttpServerFactoryBean
 * Created by Administrator on 18-5-18.
 */
public class RmiHttpInvokerExporterProvider extends RMIProvider {
    @Override
    public String publicService(Remote remote, String host, int port) {
        String url = null;
        try {
            final String name = remote.getClass().getInterfaces()[0].getName();
            url = DConstants.HTTP_INVOKER_PATH + name;
            final SimpleHttpInvokerServiceExporter exporter = new SimpleHttpInvokerServiceExporter();
            exporter.setServiceInterface(remote.getClass().getInterfaces()[0]);
            exporter.setService(remote);
            exporter.afterPropertiesSet();

            final SimpleHttpServerFactoryBean http = new SimpleHttpServerFactoryBean();
            http.setPort(DConstants.HTTP_INVOKER_PORT);
            final String finalUrl = url;
            http.setContexts(new HashMap<String, HttpHandler>() {{
                put(finalUrl, exporter);
            }});
            http.afterPropertiesSet();
            logger.info("service registered success ... ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new StringBuffer()
                .append("http://")
                .append(host)
                .append(":")
                .append(DConstants.HTTP_INVOKER_PORT)
                .append(url)
                .toString();
    }
}
