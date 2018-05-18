package org.rmi.client.consumer;

import org.rmi.client.RMIConsumer;

/**
 * 路由测试类型
 * Created by Administrator on 18-5-18.
 */
public class RmiRoutingConsumer {

    public static RMIConsumer getTypeRmiConsumer(int type){
        switch (type){
            case 0: return new RmiTraditionConsumer();
            case 1: return new RmiHttpInvokerConsumer();
            case 2: return new RmiHessionConsumer();
            default: return new RmiTraditionConsumer();
        }
    }
}
