package com.markliu.tiny4j.aop;

import com.markliu.tiny4j.annotation.Transaction;
import com.markliu.tiny4j.db.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * author:sunnymarkliu
 * date  :16-10-5
 * time  :下午3:49
 */
public class TransactionProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result = null;
        Method targetMethod = proxyChain.getTargetMethod();
        // 判断是否标注 Transaction 注解
        if (targetMethod.isAnnotationPresent(Transaction.class)) {

            try {
                LOGGER.info("begain transaction...");
                // 开启事务
                DatabaseHelper.begainTransaction();
                result = proxyChain.doProxyChain();
                // 提交事务
                LOGGER.info("commit transaction...");
                DatabaseHelper.commitTransaction();
            } catch (Exception e) {
                LOGGER.error("transaction error...", e);
                LOGGER.info("transaction error...");
                DatabaseHelper.rollbackTransaction();
            }

        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
