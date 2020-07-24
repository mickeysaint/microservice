package com.xyz.ms.common.quartz.service;

import com.xyz.base.po.quartz.JobLogPo;
import com.xyz.base.service.BaseService;
import com.xyz.ms.common.quartz.dao.JobLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobLogService extends BaseService<JobLogPo>  {

    Logger logger = LoggerFactory.getLogger(JobLogService.class);

    @Autowired
    private JobLogDao jobLogDao;

    @Autowired
    public void setDao(JobLogDao dao) {
        this.dao = dao;
    }

    public void writeJobLog(Long jobId, Date beginTime, Date endTime, boolean exeResult, String errorMsg) {
        if (endTime == null) {
            JobLogPo jobLogPo = new JobLogPo();
            jobLogPo.setJobId(jobId);
            jobLogPo.setBeginTime(beginTime);
            this.save(jobLogPo);
        } else {
            JobLogPo eg = new JobLogPo();
            eg.setJobId(jobId);
            eg.setBeginTime(beginTime);
            List<JobLogPo> list = this.findByEg(eg);
            if (list != null && list.size() > 0) {
                JobLogPo po = list.get(0);
                po.setEndTime(endTime);
                po.setExeResult(exeResult?"S":"E");
                po.setErrorMsg(errorMsg);
                this.update(po);
            }
        }
    }
}
