package com.xyz.ms.common.quartz.job;

import com.xyz.base.po.common.quartz.JobPo;
import com.xyz.ms.common.quartz.bean.ApplicationContextHolder;
import com.xyz.ms.common.quartz.service.JobLogService;
import com.xyz.ms.common.quartz.service.JobService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;

public class JobBean extends QuartzJobBean {

    protected static final Logger logger = LoggerFactory.getLogger(JobBean.class);

    private Long jobId;

    private Date beginTime;

    private Date endTime;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        preExecute(jobExecutionContext);

        boolean success = true;
        String errorMsg = null;
        try {
            doExecute(jobExecutionContext);
        } catch (Exception e) {
            logger.error("执行定时任务失败，定时任务ID=" + jobId, e);
            success = false;
            errorMsg = ExceptionUtils.getStackTrace(e);
        }

        postExecute(jobExecutionContext, success, errorMsg);
    }

    private void doExecute(JobExecutionContext jobExecutionContext) throws Exception {
        JobService jobService = ApplicationContextHolder.getApplicationContext().getBean(
                com.xyz.ms.common.quartz.service.JobService.class);
        JobPo job = jobService.findById(jobId);
        Class clazz = Class.forName(job.getJobClassName());
        Object bean = ApplicationContextHolder.getApplicationContext().getBean(clazz);
        Method method = bean.getClass().getMethod(job.getJobMethod());
        method.invoke(bean);
    }

    private void postExecute(JobExecutionContext jobExecutionContext, boolean exeResult, String errorMsg) {
        endTime = new Date();
        Long runDuration = endTime.getTime() - beginTime.getTime();

        JobService jobService = ApplicationContextHolder.getApplicationContext().getBean(
                com.xyz.ms.common.quartz.service.JobService.class);
        jobService.updateJobForPostExecute(jobId, runDuration, exeResult);
        JobLogService jobLogService = ApplicationContextHolder.getApplicationContext().getBean(
                com.xyz.ms.common.quartz.service.JobLogService.class);
        jobLogService.writeJobLog(jobId, beginTime, endTime, exeResult, errorMsg);
    }

    private void preExecute(JobExecutionContext jobExecutionContext) {
        beginTime = new Date();
        JobService jobService = ApplicationContextHolder.getApplicationContext().getBean(
                com.xyz.ms.common.quartz.service.JobService.class);
        jobService.updateJobForPreExecute(jobId, jobExecutionContext.getFireTime(), jobExecutionContext.getNextFireTime());
        JobLogService jobLogService = ApplicationContextHolder.getApplicationContext().getBean(
                com.xyz.ms.common.quartz.service.JobLogService.class);
        jobLogService.writeJobLog(jobId, beginTime, null, false, null);
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
