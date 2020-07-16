package com.xyz.ms.common.quartz.listener;

import com.xyz.ms.common.quartz.service.JobService;
import org.apache.commons.lang3.StringUtils;
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
    private JobService jobService;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("StartupListener...desdroyed....");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("StartupListener...init....");
        logger.info("开始初始化定时任务。。。");
        jobService.initJobs();
        logger.info("初始化定时任务完成。");
    }

}
