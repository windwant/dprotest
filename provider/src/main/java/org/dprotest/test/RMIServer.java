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
        zookeeperPublishSvr(2099);
    }

    public static void zookeeperPublishSvr(int port){
        ZookeeperHelloService provider = new ZookeeperHelloService();
        HelloService helloService = null;
        try {
            helloService = new HelloServiceImpl();
            provider.publish(helloService, "localhost", port);

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
