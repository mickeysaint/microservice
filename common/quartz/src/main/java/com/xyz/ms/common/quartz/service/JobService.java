package com.xyz.ms.common.quartz.service;

import com.xyz.base.po.common.quartz.JobPo;
import com.xyz.base.service.BaseService;
import com.xyz.ms.common.quartz.dao.JobDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobService extends BaseService<JobPo>  {

    Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobDao jobDao;

    @Autowired
    public void setDao(JobDao dao) {
        this.dao = dao;
    }

    @Autowired
    private QuartzJobService quartzJobService;

    public void initJobs() {
        List<JobPo> jobList = jobDao.getAllJobs();
        if (jobList != null && jobList.size() > 0) {
            for (JobPo job : jobList) {
                try {
                    if (StringUtils.equals(job.getEnabled(), "Y")) {
                        quartzJobService.addJob(job.getId(), job.getJobName(), job.getJobGroup(), job.getCron());
                    } else {
                        quartzJobService.deleteJob(job.getJobName(), job.getJobGroup());
                    }
                } catch(Exception e) {
                    logger.error("初始化定时任务失败：" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * WAITING:等待
     * PAUSED:暂停
     * RUNNING:正常执行
     * BLOCKED：阻塞
     * ERROR：错误
     */
    public void updateJobForPreExecute(Long jobId, Date fireTime, Date nextFireTime) {
        JobPo jobPo = this.findById(jobId);
        jobPo.setRuntimeLast(fireTime);
        jobPo.setRuntimeNext(nextFireTime);
        jobPo.setJobStatus("RUNNING");
        jobPo.setRunTimes((jobPo.getRunTimes()==null?0L:jobPo.getRunTimes()) + 1);
        this.update(jobPo);
    }

    public void updateForPostExecute(Long jobId, Long runDuration, boolean exeResult) {
        JobPo jobPo = this.findById(jobId);
        jobPo.setJobStatus(exeResult?"WAITING":"ERROR");
        jobPo.setRunDuration((jobPo.getRunDuration()==null?0L:jobPo.getRunDuration()) + runDuration);
        this.update(jobPo);
    }
}
