package org.rmi.server;

import org.rmi.common.DConstants;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.rmi.Remote;

/**
 * RMI SPRING SERVER
 * Created by windwant on 2016/6/29.
 */
public class RMISpringServer extends RMIProvider {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ctx.start();
    }

    @Override
    public String publicService(Remote remote, String host, int port) {
        final String name = remote.getClass().getInterfaces()[0].getName();
        String url = DConstants.HTTP_INVOKER_PATH + name;
        return new StringBuffer()
                .append("http://")
                .append(host)
                .append(":")
                .append(DConstants.HTTP_INVOKER_PORT)
                .append(url)
                .toString();
    }
}
