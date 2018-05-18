package org.rmi.client.consumer;

import org.rmi.client.RMIConsumer;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

/**
 * HttpInvokerProxyFactoryBean
 *
 * Created by windwant on 2016/6/29.
 */
public class RmiHessionConsumer extends RMIConsumer{

    @Override
    public <T> T lookupService(String url) {
        T remote = null;
        HessianProxyFactoryBean invoker = new HessianProxyFactoryBean();
        invoker.setServiceUrl(url);
        try {
            invoker.setServiceInterface(Class.forName(url.substring(url.lastIndexOf("/") + 1)));
            invoker.afterPropertiesSet();
            remote = (T) invoker.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remote;
    }
}
