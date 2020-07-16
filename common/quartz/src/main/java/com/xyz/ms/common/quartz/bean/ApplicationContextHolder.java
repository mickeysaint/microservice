package com.xyz.ms.common.quartz.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext ac = null;

    public static ApplicationContext getApplicationContext() {
        return ac;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextHolder.ac = applicationContext;
    }
}
