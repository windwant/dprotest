package org.rmi.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.rmi.common.DConstants;

/**
 * Created by windwant on 2016/6/29.
 */
public class RMIProvider {
    private static final Logger logger = LogManager.getLogger(RMIProvider.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public void publish(Remote remote, String host, int port){
        String url = publicService(remote, host, port);//注册服务
        if(url != null){
            ZooKeeper zk = connectServer(); //连接zookeeper
            if(zk != null){
                createNode(zk, url); //创建服务节点
            }
        }
    }

    private String publicService(Remote remote, String host, int port){
        String url = null;
        try {
            url = String.format("rmi://%s:%d/%s", host, port, remote.getClass().getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url, remote);
            logger.info("service registered success ... ");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private ZooKeeper connectServer(){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(DConstants.ZK_HOST_PORT, 5000, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) { //连接成功事件
                        // 放开闸门, wait在connect方法上的线程将被唤醒
                        latch.countDown();
                        logger.info("zookeeper connected success ...");
                    }

                }
            });
            latch.await();
            Stat stat = zk.exists(DConstants.ZK_ROOT_PATH, false); //是否已创建路径
            if(stat==null) {
                String path = zk.create(DConstants.ZK_ROOT_PATH, "rmi".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//根路径设置为持久节点 任何应用程序在节点上可进行任何操作，能创建、列出和删除它的子节点
            }
            logger.info("zookeeper root node: {} created success ...", "rmi");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String url){
        try {
            String path = zk.create(DConstants.ZK_ROOT_PATH  + DConstants.ZK_BUSI_PATH, url.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); //服务节点设置为临时节点，服务失效则节点删除
            logger.info("zookeeper service node: {} created success ...", url);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
