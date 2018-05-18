package org.rmi.common;

/**
 * Created by windwant on 2016/6/29.
 */
public class DConstants {
    public static final String ZK_HOST_PORT = "localhost:2181";

    public static final int ZK_SESSION_TIMEOUT = 5000;

    public static final String ZK_ROOT_PATH = "/registry";

    public static final String ZK_BUSI_PATH = "/provider";

    public static final String RMI_REGISTRY_HOST = "localhost";

    public static final int RMI_REGISTRY_PORT = 1099;

    public static final String HTTP_INVOKER_PATH = "/remoting/";

    public static final int HTTP_INVOKER_PORT = 8589;

    public static final int TEST_TYPE = 0;// 测试类型 [0, 1, 2] O: Tradition 1: HttpInvoker 2: Hessian
}
