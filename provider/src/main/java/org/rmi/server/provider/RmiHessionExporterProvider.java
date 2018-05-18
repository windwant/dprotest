package org.rmi.server.provider;

import org.rmi.common.DConstants;
import org.rmi.common.SimpleHttpServerUtil;
import org.rmi.server.RMIProvider;
import org.springframework.remoting.caucho.SimpleHessianServiceExporter;

import java.rmi.Remote;

/**
 * SimpleHttpInvokerServiceExporter
 * SimpleHttpServerFactoryBean
 * Created by Administrator on 18-5-18.
 */
public class RmiHessionExporterProvider extends RMIProvider {
    @Override
    public String publicService(Remote remote, String host, int port) {
        String url = null;
        try {
            final String name = remote.getClass().getInterfaces()[0].getName();
            url = DConstants.HTTP_INVOKER_PATH + name;
            final SimpleHessianServiceExporter exporter = new SimpleHessianServiceExporter();
            exporter.setServiceInterface(remote.getClass().getInterfaces()[0]);
            exporter.setService(remote);
            exporter.afterPropertiesSet();

            SimpleHttpServerUtil.startSimpleHttpServer(url, exporter);
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
