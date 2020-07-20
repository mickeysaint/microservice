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

    public void addJob(Long jobId, String jobName, String jobGroup, String cron) throws SchedulerException {
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

    }

    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }

        logger.info("删除定时任务:" + jobKey.toString());
        scheduler.deleteJob(jobKey);
    }

    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName + "_trigger", jobGroup + "_trigger");
        scheduler.pauseTrigger(triggerKey);
    }

    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName + "_trigger", jobGroup + "_trigger");
        scheduler.resumeTrigger(triggerKey);
    }

    public void runJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
    }
}
