package com.xyz.ms.common.quartz.dao;

import com.xyz.base.po.quartz.JobPo;
import com.xyz.base.service.BaseDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jobDao")
public class JobDao extends BaseDao<JobPo> {

    public List<JobPo> getAllJobs() {
        String sql = "select * from qrtzx_job ";
        return this.findBySql(sql, null);
    }

    public JdbcTemplate getJt() {
        return this.jt;
    }
}
