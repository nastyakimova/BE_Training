package com.github.test.service.aspect;

import com.github.test.dao.JdbcConnectionHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class TransactionalSpringAspect {
    private final JdbcConnectionHolder connectionHolder;

    public TransactionalSpringAspect(JdbcConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Around("within(com.github.test.service.DogServiceImpl)")
    public Object invoke(ProceedingJoinPoint joinPoint) {
        Object result;
        try {
            result = joinPoint.proceed();
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        log.info("After invoking join point: {}", joinPoint.toString());
        return result;
    }
}
