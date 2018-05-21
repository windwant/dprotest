package org.rmi.client;

import org.rmi.common.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.rmi.RemoteException;

/**
 * RMI SPRING SERVER
 * Created by windwant on 2016/6/29.
 */
public class RMISpringConsumer {

    public static void main(String[] args) throws RemoteException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ctx.start();
        HelloService consumer = (HelloService) ctx.getBean("consumer");
        System.out.println(consumer.hello("jack"));
    }
}
