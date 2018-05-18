package org.rmi.server.provider;

import org.rmi.server.RMIProvider;

/**
 * 路由测试类型
 *
 * Created by Administrator on 18-5-18.
 */
public class RmiRoutingProvider {

    public static RMIProvider getTypeRmipRrovider(int type){
        switch (type){
            case 0: return new RmiTraditionProvider();
            case 1: return new RmiHttpInvokerExporterProvider();
            case 2: return new RmiHessionExporterProvider();
            default: return new RmiTraditionProvider();
        }
    }
}
