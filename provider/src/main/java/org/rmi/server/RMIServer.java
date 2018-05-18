package org.rmi.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rmi.common.DConstants;
import org.rmi.common.HelloService;
import org.rmi.server.impl.HelloServiceImpl;
import org.rmi.server.provider.RmiHttpInvokerExporterProvider;
import org.rmi.server.provider.RmiTraditionProvider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * RMI SERVER
 * Created by windwant on 2016/6/29.
 */
public class RMIServer {
    private static final Logger logger = LogManager.getLogger(RMIServer.class);

    public static void main(String[] args) {
        int port = DConstants.RMI_REGISTRY_PORT;
        if(args != null && args.length > 0 && args[0] != null) {
            port = Integer.parseInt(args[0]);
        }
        RMIProvider provider = new RmiTraditionProvider(); //RmiHttpInvokerExporterProvider();
        HelloService helloService = null;
        try {
            helloService = new HelloServiceImpl();
            provider.publish(helloService, DConstants.RMI_REGISTRY_HOST, port);

            logger.info("rmi service publish and start success ... ");
            System.in.read();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void jndiPublishSvr(){
        int port = 1099;
        String url = "rmi://localhost:1099/HelloServiceImpl";
        try {
            LocateRegistry.createRegistry(port);
            Naming.rebind(url, new HelloServiceImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
