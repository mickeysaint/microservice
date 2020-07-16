package com.xyz.ms.common.quartz.job;

import org.springframework.stereotype.Service;

@Service
public class HelloJob {

    public void execute() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("hello2...");
    }

}
