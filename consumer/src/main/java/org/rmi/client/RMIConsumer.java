package org.rmi.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.rmi.common.DConstants;
import java.io.IOException;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by windwant on 2016/6/29.
 */
public abstract class RMIConsumer {
    public static final Logger logger = LogManager.getLogger(RMIConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> urlList = new ArrayList<String>();

    public RMIConsumer(){
        ZooKeeper zk = connectServer();
        if(zk != null){
            watchNode(zk);
        }
    }

    // 连接 ZooKeeper 服务器
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(DConstants.ZK_HOST_PORT, 5000, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            latch.await(); // 使当前线程处于等待状态
        } catch (IOException e) {
            logger.error("", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }

    private void watchNode(final ZooKeeper zk){
        try {
            List<String> nodeList = zk.getChildren(DConstants.ZK_ROOT_PATH, new Watcher() {
                public void process(WatchedEvent event) {
                    if(event.getType() == Event.EventType.NodeChildrenChanged){ //监听服务节点变更事件
                        watchNode(zk);//重新注册
                    }
                }
            });
            List<String> data = new ArrayList<String>();
            for(String node: nodeList){
                data.add(new String(zk.getData(DConstants.ZK_ROOT_PATH + "/" + node, false, null)));
            }
            urlList = data;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机获取服务 服务都实现了Remote接口，泛型
     * @param <T>
     * @return
     */
    public <T extends Remote> T lookup(){
        T service = null;
        int size = urlList.size();
        if(size > 0){
            String url;
            if(size == 1){
                url = urlList.get(0);
            }else{
                url = urlList.get(ThreadLocalRandom.current().nextInt(size));
            }
            service = lookupService(url);
        }
        return service;
    }

    /**
     * 模板方法 获取服务
     * @param url
     * @param <T>
     * @return
     */
    public abstract  <T> T lookupService(String url);
}
