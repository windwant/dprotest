package org.dprotest.test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by aayongche on 2016/6/29.
 */
public class RMIClient {
    public static void main(String[] args) {
        zookeeperTestClient();
    }

    public static void zookeeperTestClient(){
        ZookeeperConsumer consumer = new ZookeeperConsumer();
        try {
            while (true) {
                HelloService helloService = consumer.lookup();
                String result = null;

                result = helloService.hello("Jack");
                System.out.println(result);
                Thread.sleep(3000);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void jndiTestClient(){
        String url = "rmi://localhost:1099/org.dprotest.test.impl.HelloServiceImpl";
        try {
            HelloService s = (HelloService) Naming.lookup(url);
            for (int i = 0; i < 10; i++) {
                System.out.println(s.hello("lilei" + i));
                Thread.sleep(1000);
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
