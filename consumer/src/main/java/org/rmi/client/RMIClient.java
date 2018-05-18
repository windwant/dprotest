package org.rmi.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rmi.client.consumer.RmiTraditionConsumer;
import org.rmi.common.HelloService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by windwant on 2016/6/29.
 */
public class RMIClient {
    private static final Logger logger = LogManager.getLogger(RMIClient.class);
    public static void main(String[] args) throws IOException {
        try {
            RMIConsumer consumer = new RmiTraditionConsumer(); //new RmiHttpInvokerConsumer();
            while (true) {
                HelloService helloService = consumer.lookup();
                String result = null;

                result = helloService.hello("Jack");
                logger.info("rmi result: {}", result);
                Thread.sleep(3000);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void jndiTestClient(){
        String url = "rmi://localhost:1099/HelloServiceImpl";
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
