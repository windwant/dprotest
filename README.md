# rmi-service

## 分布式

### rmi 应用实例

>* zookeeper服务注册，发现，管理

>* common：通用接口，工具类

>* provider: rmi 服务提供者
>    * Tradition origin register
>    * HttpInvoker Exporter
>    * Hessian Exporter

>* consumer：rmi 服务消费者
>    * Tradition origin
>    * HttpInvoker HttpInvokerProxy
>    * Hessian HessianProxy


### 测试：

>* DConstants
>    * public static final int TEST_TYPE = 0;// 测试类型 [0, 1, 2] O: Tradition 1: HttpInvoker 2: Hessian

