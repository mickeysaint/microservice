package com.xyz.ms.common.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients // 要使用Feign，需要加上此注解
public class QuartzApplication {
    public static void main(String[] args) {
//        try {
//            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//
//            JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job_2", "group_2").build();
//
//            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("test", "test_group")
//                    .startNow().withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")).build();
//            scheduler.scheduleJob(jobDetail, trigger);
//
//            scheduler.start();
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        SpringApplication.run(QuartzApplication.class, args);

    }
}
