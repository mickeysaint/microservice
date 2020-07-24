package com.xyz.base.po.quartz;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name="qrtzx_job_log")
public class JobLogPo extends BasePo {
    @Column(name="ID")
    private Long id;

    @Column(name="JOB_ID")
    private Long jobId;

    @Column(name="BEGIN_TIME")
    private Date beginTime;

    @Column(name="END_TIME")
    private Date endTime;

    @Column(name="EXE_RESULT")
    private String exeResult;

    @Column(name="ERROR_MSG")
    private String errorMsg;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getExeResult() {
        return exeResult;
    }

    public void setExeResult(String exeResult) {
        this.exeResult = exeResult;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
