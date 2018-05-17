package org.rmi.server.impl;

import org.rmi.common.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by windwant on 2016/6/29.
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    public HelloServiceImpl() throws RemoteException{};

    public String hello(String name) throws RemoteException {
        return String.format("%s say hello!", name);
    }
}
