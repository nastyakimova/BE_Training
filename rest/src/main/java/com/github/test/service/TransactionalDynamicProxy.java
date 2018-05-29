package com.github.test.service;

import com.github.test.dao.JdbcConnectionHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionalDynamicProxy implements InvocationHandler {
    private DogService invocationTarget;
    private JdbcConnectionHolder connectionHolder;

    public TransactionalDynamicProxy(DogService invocationTarget, JdbcConnectionHolder connectionHolder) {
        this.invocationTarget = invocationTarget;
        this.connectionHolder = connectionHolder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            result = method.invoke(invocationTarget, args);
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static DogService newInstance(DogService target, JdbcConnectionHolder connectionHolder) {
        return (DogService) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionalDynamicProxy(target, connectionHolder));
    }
}
