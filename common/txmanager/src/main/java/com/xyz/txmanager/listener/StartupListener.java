package com.xyz.txmanager.listener;

import com.xyz.txmanager.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class StartupListener implements ServletContextListener {

    Logger logger = LoggerFactory.getLogger(StartupListener.class);

    @Autowired
    private TransactionService transactionService;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("StartupListener...desdroyed....");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("StartupListener...init....");
        logger.info("开始初始化线程池。。。");
        transactionService.initThreadPool();
        logger.info("初始化线程池完成。");
    }

}
