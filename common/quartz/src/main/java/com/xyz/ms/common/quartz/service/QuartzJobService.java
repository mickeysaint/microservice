package com.xyz.ms.common.quartz.service;

import com.xyz.ms.common.quartz.job.JobBean;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class QuartzJobService {
    Logger logger = LoggerFactory.getLogger(QuartzJobService.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public void addJob(Long jobId, String jobName, String jobGroup, String cron) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                logger.info("job:" + jobKey.toString() + " 已存在");
            } else {
                //构建job信息
                jobDetail = JobBuilder.newJob(JobBean.class).withIdentity(jobName, jobGroup).build();
                //用JopDataMap来传递数据
                jobDetail.getJobDataMap().put("jobId", jobId.toString());

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

                //按新的cronExpression表达式构建一个新的trigger
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName + "_trigger", jobGroup + "_trigger")
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            logger.error("新增任务失败", e);
        }

    }

    public void deleteJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
            logger.info("删除定时任务:" + jobKey.toString());
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("删除任务失败", e);
        }

    }
}
