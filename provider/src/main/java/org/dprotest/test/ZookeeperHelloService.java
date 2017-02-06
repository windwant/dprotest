package org.dprotest.test;

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

/**
 * Created by windwant on 2016/6/29.
 */
public class ZookeeperHelloService {
    private static final Logger logger = LogManager.getLogger(ZookeeperHelloService.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public void publish(Remote remote, String host, int port){
        String url = publicService(remote, host, port);
        if(url != null){
            ZooKeeper zk = connectServer();
            if(zk != null){
                createNode(zk, url);
            }
        }
    }

    private String publicService(Remote remote, String host, int port){
        String url = null;
        try {
            url = String.format("rmi://%s:%d/%s", host, port, remote.getClass().getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url, remote);
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
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        // 放开闸门, wait在connect方法上的线程将被唤醒
                        latch.countDown();
                    }

                }
            });
            latch.await();
            Stat stat = zk.exists(DConstants.ZK_ROOT_PATH, false);
            if(stat==null) {
                String path = zk.create(DConstants.ZK_ROOT_PATH, "rmi".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
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
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
