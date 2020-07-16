package com.xyz.base.po.common.quartz;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name="qrtzx_job")
public class JobPo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="JOB_NAME")
    private String jobName;

    @Column(name="JOB_GROUP")
    private String jobGroup;

    @Column(name="CRON")
    private String cron;

    @Column(name="JOB_CLASS_NAME")
    private String jobClassName;

    @Column(name="JOB_METHOD")
    private String jobMethod;

    @Column(name="RUNTIME_LAST")
    private Date runtimeLast;

    @Column(name="RUNTIME_NEXT")
    private Date runtimeNext;

    /**
     * WAITING:等待
     * PAUSED:暂停
     * RUNNING:正常执行
     * BLOCKED：阻塞
     * ERROR：错误
     */
    @Column(name="JOB_STATUS")
    private String jobStatus;

    @Column(name="ENABLED")
    private String enabled;

    @Column(name="RUN_TIMES")
    private Long runTimes;

    @Column(name="RUN_DURATION")
    private Long runDuration;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobMethod() {
        return jobMethod;
    }

    public void setJobMethod(String jobMethod) {
        this.jobMethod = jobMethod;
    }

    public Date getRuntimeLast() {
        return runtimeLast;
    }

    public void setRuntimeLast(Date runtimeLast) {
        this.runtimeLast = runtimeLast;
    }

    public Date getRuntimeNext() {
        return runtimeNext;
    }

    public void setRuntimeNext(Date runtimeNext) {
        this.runtimeNext = runtimeNext;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Long getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(Long runTimes) {
        this.runTimes = runTimes;
    }

    public Long getRunDuration() {
        return runDuration;
    }

    public void setRunDuration(Long runDuration) {
        this.runDuration = runDuration;
    }
}
