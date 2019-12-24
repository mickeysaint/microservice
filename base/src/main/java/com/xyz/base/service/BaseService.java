package com.xyz.base.service;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.po.BasePo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BaseService<T extends BasePo> {

    protected BaseDao<T> dao = null;

    public abstract void setDao(BaseDao<T> dao);

    public T findById(long id) {
        return dao.findById(id);
    }

    @Transactional
    public void save(T t) {
        dao.save(t);
    }

    @Transactional
    public void update(T t) {
        dao.update(t);
    }

    @Transactional
    public void delete(T t) {
        dao.delete(t);
    }

    public List<T> findByEg(T eg) {
        return dao.findByEg(eg);
    }

    public List<T> findByEg(T eg, String orderBy) {
        return dao.findByEg(eg, orderBy);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        dao.deleteByIds(ids);
    }
}
