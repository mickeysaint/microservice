package com.xyz.ms.common.quartz.job;

import org.springframework.stereotype.Service;

@Service
public class HelloJob {

    public void execute() throws InterruptedException {
        System.out.println("hello...1");
        Thread.sleep(20*1000);
        System.out.println("hello...2");
        Thread.sleep(20*1000);
        System.out.println("hello...3");
    }

}
