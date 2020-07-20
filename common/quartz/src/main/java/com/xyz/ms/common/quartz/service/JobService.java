package com.xyz.ms.common.quartz.service;

import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.common.quartz.JobPo;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.StringUtil;
import com.xyz.ms.common.quartz.dao.JobDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        if (nextFireTime != null) {
            jobPo.setRuntimeNext(nextFireTime);
        }
        jobPo.setJobStatus("RUNNING");
        jobPo.setRunTimes((jobPo.getRunTimes()==null?0L:jobPo.getRunTimes()) + 1);
        this.update(jobPo);
    }

    public void updateJobForPostExecute(Long jobId, Long runDuration, boolean exeResult) {
        JobPo jobPo = this.findById(jobId);
        jobPo.setJobStatus(exeResult ? "WAITING" : "ERROR");
        jobPo.setRunDuration((jobPo.getRunDuration()==null?0L:jobPo.getRunDuration()) + runDuration);
        this.update(jobPo);
    }

    public Page<JobPo> getListData(Map params) {
        String jobName = StringUtil.objToString(params.get("jobName"));
        String jobGroup = StringUtil.objToString(params.get("jobGroup"));
        Long pageIndex = StringUtil.objToLong(params.get("pageIndex"));
        pageIndex = pageIndex==null?1:pageIndex;
        Long pageSize = StringUtil.objToLong(params.get("pageSize"));
        pageSize = pageSize==null? Constants.PAGE_SIZE_DEFAULT :pageSize;
        JobPo eg = new JobPo();
        eg.setJobName(jobName);
        eg.setJobGroup(jobGroup);
        return jobDao.findPageByEg(eg, "order by id desc ", pageIndex, pageSize);
    }

    public boolean exists(String jobName, String jobGroup) {
        JobPo eg = new JobPo();
        eg.setJobName(jobName);
        eg.setJobGroup(jobGroup);
        List<JobPo> list = dao.findByEg(eg);
        if (list != null && list.size() > 0) {
            return true;
        }

        return false;
    }

    @Transactional
    public void saveJob(JobPo jobPo) {
        if (jobPo.getId() == null) {
            jobPo.setJobStatus("WAITING");
            jobPo.setEnabled("Y");
            jobDao.save(jobPo);
        } else {
            JobPo jobPoOld = findById(jobPo.getId());

            // 只有启用状态的才能被更新
            if (!StringUtils.equals(jobPoOld.getEnabled(), "Y")) {
                throw new BusinessException("该任务状态不允许被更新。");
            }

            if (StringUtils.equals(jobPoOld.getJobStatus(), "WAITING")
                    || StringUtils.equals(jobPoOld.getJobStatus(), "ERROR")) {
                jobPoOld.setJobStatus("WAITING");
                jobPoOld.setEnabled("Y");
                jobPoOld.setCron(jobPo.getCron());
                jobPoOld.setJobClassName(jobPo.getJobClassName());
                jobPoOld.setJobMethod(jobPo.getJobMethod());
                jobDao.update(jobPoOld);
                jobPo = jobPoOld;
            } else {
                throw new BusinessException("该任务状态不允许被更新。");
            }
        }

        try {
            quartzJobService.deleteJob(jobPo.getJobName(), jobPo.getJobGroup());
            quartzJobService.addJob(jobPo.getId(), jobPo.getJobName(), jobPo.getJobGroup(), jobPo.getCron());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void pauseJob(Long id) {
        JobPo jobPo = findById(id);

        // 只有启用状态的才能被更新
        if (!StringUtils.equals(jobPo.getEnabled(), "Y")) {
            throw new BusinessException("该任务状态不允许被更新。");
        }

        if (!StringUtils.equals(jobPo.getJobStatus(), "RUNNING")) {
            throw new BusinessException("只有正在执行的任务才能被暂停。");
        }

        jobPo.setJobStatus("PAUSED");
        jobDao.update(jobPo);

        try {
            quartzJobService.pauseJob(jobPo.getJobName(), jobPo.getJobGroup());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void resumeJob(Long id) {
        JobPo jobPo = findById(id);

        // 只有启用状态的才能被更新
        if (!StringUtils.equals(jobPo.getEnabled(), "Y")) {
            throw new BusinessException("该任务状态不允许被更新。");
        }

        if (!StringUtils.equals(jobPo.getJobStatus(), "PAUSED")) {
            throw new BusinessException("只有暂停的任务才能被继续执行。");
        }

        jobPo.setJobStatus("RUNNING");
        jobDao.update(jobPo);

        try {
            quartzJobService.resumeJob(jobPo.getJobName(), jobPo.getJobGroup());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void runJob(Long id) {
        JobPo jobPo = findById(id);

        // 只有启用状态的才能被更新
        if (!StringUtils.equals(jobPo.getEnabled(), "Y")) {
            throw new BusinessException("该任务状态不允许被更新。");
        }

        if (!StringUtils.equals(jobPo.getJobStatus(), "WAITING")) {
            throw new BusinessException("只有等待的任务才能被执行。");
        }

        try {
            quartzJobService.runJob(jobPo.getJobName(), jobPo.getJobGroup());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void disabledJob(Long id) {
        JobPo jobPo = findById(id);
        jobPo.setEnabled("N");
        jobDao.update(jobPo);

        try {
            quartzJobService.deleteJob(jobPo.getJobName(), jobPo.getJobGroup());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void enabledJob(Long id) {
        JobPo jobPo = findById(id);
        jobPo.setEnabled("Y");
        jobDao.update(jobPo);

        try {
            quartzJobService.addJob(jobPo.getId(), jobPo.getJobName(), jobPo.getJobGroup(), jobPo.getCron());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }
}
