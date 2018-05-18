package org.rmi.client.consumer;

import org.rmi.client.RMIConsumer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by windwant on 2016/6/29.
 */
public class RmiTraditionConsumer extends RMIConsumer{

    @Override
    public <T> T lookupService(String url) {
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
