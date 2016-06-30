package org.dprotest.test;

import org.dprotest.test.impl.HelloServiceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by aayongche on 2016/6/29.
 */
public class RMIServer {
    public static void main(String[] args) {
        int port = DConstants.RMI_REGISTRY_PORT;
        if(args != null && args.length > 0 && args[0] != null) {
            port = Integer.parseInt(args[0]);
        }
        zookeeperPublishSvr(DConstants.RMI_REGISTRY_HOST, port);
    }

    public static void zookeeperPublishSvr(String host, int port){
        ZookeeperHelloService provider = new ZookeeperHelloService();
        HelloService helloService = null;
        try {
            helloService = new HelloServiceImpl();
            provider.publish(helloService, host, port);

            Thread.sleep(Long.MAX_VALUE);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void jndiPublishSvr(){
        int port = 1099;
        String url = "rmi://localhost:1099/org.dprotest.test.impl.HelloServiceImpl";
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
