package com.github.test.service;

import com.github.test.dao.JdbcConnectionHolder;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class CglibTransactionalDogService implements InvocationHandler {
    private final DogService invocationTarget;
    private final JdbcConnectionHolder connectionHolder;

    public CglibTransactionalDogService(DogService dogService, JdbcConnectionHolder connectionHolder) {
        this.invocationTarget = dogService;
        this.connectionHolder = connectionHolder;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object result;
        try {
            result = method.invoke(invocationTarget, objects);
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static DogServiceImpl newInstance(final DogService invocationTarget, final JdbcConnectionHolder connectionHolder) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(invocationTarget.getClass());
        enhancer.setCallback(new CglibTransactionalDogService(invocationTarget, connectionHolder));
        return (DogServiceImpl) enhancer.create();
    }


}
