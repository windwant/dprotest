package org.dprotest.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by aayongche on 2016/6/29.
 */
public class ZookeeperConsumer {
    private static final Logger logger = LogManager.getLogger(ZookeeperConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> urlList = new ArrayList<String>();

    ZookeeperConsumer(){
        ZooKeeper zk = connectServer();
        if(zk != null){
            watchNode(zk);
        }
    }

    // 连接 ZooKeeper 服务器
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper("192.168.7.162:2181", 5000, new Watcher() {
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
            List<String> nodeList = zk.getChildren("/registry", new Watcher() {
                public void process(WatchedEvent event) {
                    if(event.getType() == Event.EventType.NodeChildrenChanged){
                        watchNode(zk);
                    }
                }
            });
            List<String> data = new ArrayList<String>();
            for(String node: nodeList){
                data.add(new String(zk.getData("/registry/" + node, false, null)));
            }
            urlList = data;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    private <T> T lookupService(String url){
        T remote = null;
        try {
            remote = (T) Naming.lookup(url);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return remote;
    }
}
