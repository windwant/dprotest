package org.rmi.server.provider;

import org.rmi.common.RMIUtils;
import org.rmi.server.RMIProvider;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * LocateRegistry
 * Created by Administrator on 18-5-18.
 */
public class RmiTraditionProvider extends RMIProvider {
    @Override
    public String publicService(Remote remote, String host, int port) {
        String url = null;
        try {
            url = RMIUtils.registerRMIService(host, port, remote);
            logger.info("service registered success ... ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return url;
    }
}
