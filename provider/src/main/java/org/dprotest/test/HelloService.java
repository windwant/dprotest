package org.dprotest.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by aayongche on 2016/6/29.
 */
public interface HelloService extends Remote {

    public String hello(String name) throws RemoteException;
}
