package org.rmi.client.consumer;

import org.rmi.client.RMIConsumer;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Created by windwant on 2016/6/29.
 */
public class RmiProxyFactoryConsumer extends RMIConsumer{

    @Override
    public <T> T lookupService(String url) {
        T remote = null;
        RmiProxyFactoryBean factoryBean = new RmiProxyFactoryBean();
        factoryBean.setServiceUrl(url);
        try {
            factoryBean.setServiceInterface(Class.forName(url.substring(url.lastIndexOf("/") + 1)));
            factoryBean.afterPropertiesSet();
            remote = (T) factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remote;
    }
}
