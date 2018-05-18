package org.rmi.server.provider;

import org.rmi.server.RMIProvider;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Administrator on 18-5-18.
 */
public class RmiServiceExporterProvider extends RMIProvider {
    @Override
    public String publicService(Remote remote, String host, int port) {
        String url = null;
        try {
            url = String.format("rmi://%s:%d/%s", host, port, remote.getClass().getInterfaces()[0].getName());
            RmiServiceExporter exporter = new RmiServiceExporter();
            exporter.setRegistryHost(host);
            exporter.setRegistryPort(port);
            exporter.setServiceInterface(remote.getClass().getInterfaces()[0]);
            exporter.setServiceName(url);
            exporter.setService(remote);
            exporter.afterPropertiesSet();
            logger.info("service registered success ... ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return url;
    }
}
