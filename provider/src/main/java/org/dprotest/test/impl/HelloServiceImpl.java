package org.dprotest.test.impl;

import org.dprotest.test.HelloService;

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
