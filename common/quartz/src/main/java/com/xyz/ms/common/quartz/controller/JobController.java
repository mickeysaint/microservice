package com.xyz.ms.common.quartz.controller;

import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.quartz.JobPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.common.quartz.service.JobService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    Logger logger = LoggerFactory.getLogger(JobController.class);

    @RequestMapping("/findById")
    public ResultBean<JobPo> findById(Long id) {
        ResultBean<JobPo> ret = new ResultBean<>();
        JobPo jobPo = jobService.findById(id);
        ret.setData(jobPo);
        return ret;
    }

    @RequestMapping("/getListData")
    public ResultBean<Page<JobPo>> getListData(@RequestBody Map params, HttpServletRequest request) {
        ResultBean<Page<JobPo>> ret = new ResultBean<>();
        Page<JobPo> jobs = jobService.getListData(params);
        ret.setData(jobs);
        return ret;
    }

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody JobPo jobPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(jobPo != null, "没有接收到定时任务数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(jobPo.getJobName()), "定时任务名称不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(jobPo.getJobGroup()), "定时任务分类不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(jobPo.getCron()), "cron表达式不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(jobPo.getJobClassName()), "类名不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(jobPo.getJobMethod()), "方法名不能为空。");

            AssertUtils.isTrue(
                    (
                            (jobPo.getId() != null) // 更新时不校验
                            || (jobPo.getId() == null && !jobService.exists(jobPo.getJobName(), jobPo.getJobGroup())) // 创建时要校验定时任务是否存在
                    ),
                    "该定时任务已经存在。");

            jobService.saveJob(jobPo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/pause")
    public ResultBean<Void> pause(Long id) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            jobService.pauseJob(id);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/resume")
    public ResultBean<Void> resume(Long id) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            jobService.resumeJob(id);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/run")
    public ResultBean<Void> run(Long id) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            jobService.runJob(id);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/disabled")
    public ResultBean<Void> disabled(Long id) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            jobService.disabledJob(id);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/enabled")
    public ResultBean<Void> enabled(Long id) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            jobService.enabledJob(id);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }
}
