package com.xyz.base.service;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.po.BasePo;
import com.xyz.base.vo.user.RoleVo;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao<T extends BasePo> extends SqlSessionDaoSupport {

    @Autowired
    protected JdbcTemplate jt;

    @Resource(name="sqlSessionTemplate")
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    private Class<T> getPoClass() {
        Type t = this.getClass().getGenericSuperclass();
        Type[] tarr = ((ParameterizedType)t).getActualTypeArguments();
        return (Class<T>)tarr[0];
    }

    public T findById(long id) {
        String sql = buildSql_findById();
        return map2Po(jt.queryForMap(sql, new Object[]{id}));
    }

    @Transactional
    public void save(T t) {
        List<Object> params = new ArrayList<>();
        String sql = buildSql_save(t, params);
        jt.update(sql, params.toArray());
        Long id = jt.queryForObject("select last_insert_id()", Long.class);
        t.setId(id);
    }

    @Transactional
    public void update(T t) {
        List<Object> params = new ArrayList<>();
        String sql = buildSql_update(t, params);
        jt.update(sql, params.toArray());
    }

    @Transactional
    public void delete(T t) {
        List<Object> params = new ArrayList<>();
        String sql = buildSql_delete(t, params);
        jt.update(sql, params.toArray());
    }

    public List<T> findByEg(T eg) {
        return findByEg(eg, null);
    }

    public List<T> findByEg(T eg, String orderBy) {
        Assert.notNull(eg, "eg can not be null. ");
        List<Object> params = new ArrayList<>();
        String sql = buildSql_findByEg(eg, params);
        if (!StringUtils.isEmpty(orderBy)) {
            sql += " " + orderBy;
        }
        return findBySql(sql, params);
    }

    public List<T> findBySql(String sql, List<Object> params) {
        List<Map<String, Object>> mlist = jt.queryForList(sql, params.toArray());
        List<T> rlist = new ArrayList<>();
        if (CollectionUtils.isEmpty(mlist)) {
            return rlist;
        } else {
            mlist.forEach(m->{rlist.add(map2Po(m));});
            return rlist;
        }
    }

    public Page<T> findPageBySql(String sql, List<Object> params, int pageIndex, int pageSize) {
        String sqlCount = "select count(1) from (" + sql + ") t_main ";
        int offset = (pageIndex-1)*pageSize;
        String sqlList = "select * from (" + sql + ") t_main limit " + offset + ", " + pageSize;
        Page<T> page = new Page<>();
        page.setCount(jt.queryForObject(sqlCount, params.toArray(), Long.class));
        page.setDataList(findBySql(sqlList, params));
        return page;
    }

    private T map2Po(Map<String, Object> m) {
        if (m == null) {
            return null;
        }

        Class<T> clas = getPoClass();
        T t = null;
        try {
            t = clas.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        BeanWrapper bw = new BeanWrapperImpl(t);
        Map<String, String> javaDbFieldMapping = getJavaDbFieldMapping(clas);
        Map<String, String> dbJavaFieldMapping = new HashMap<String, String>();
        javaDbFieldMapping.forEach((k, v) -> {
            dbJavaFieldMapping.put(v, k);
        });

        m.forEach((k, v) -> {
            String dbFieldName = k.toUpperCase();
            String javaFieldName = dbJavaFieldMapping.get(dbFieldName);
            bw.setPropertyValue(javaFieldName, v);
        });

        return t;
    }

    private String buildSql_findById() {
        Class<T> clas = getPoClass();
        String tableName = getTableName();
        Map<String, String> javaDbFieldMapping = getJavaDbFieldMapping(clas);
        List<String> dbFieldNames = new ArrayList<>();
        javaDbFieldMapping.forEach((k, v) -> {
            dbFieldNames.add(v);
        });
        String dbIdFieldName = getDbIdFieldName();
        String format = "select %s from %s where "+dbIdFieldName+"=?";
        return String.format(format, StringUtils.join(dbFieldNames, ", "), tableName);
    }

    private String buildSql_findByEg(T eg, List<Object> params) {
        String tableName = getTableName();
        Class<T> clas = getPoClass();
        Map<String, String> javaDbFieldMapping = getJavaDbFieldMapping(clas);
        String format = "select %s from %s where %s ";

        BeanWrapper bw = new BeanWrapperImpl(eg);
        List<String> conditionList = new ArrayList<>();
        conditionList.add("1=1");

        List<String> dbFieldNames = new ArrayList<String>();
        for (Map.Entry<String, String> e : javaDbFieldMapping.entrySet()) {
            String javaFieldName = e.getKey();
            String dbFieldName = e.getValue();
            dbFieldNames.add(dbFieldName);
            Object v = bw.getPropertyValue(javaFieldName);
            if (v != null) {
                params.add(v);
                conditionList.add(dbFieldName + "=?");
            }
        }

        return String.format(format, StringUtils.join(dbFieldNames, ", "), tableName,
                StringUtils.join(conditionList, " and "));
    }

    private String buildSql_save(T t, List<Object> params) {
        BeanWrapper bw = new BeanWrapperImpl(t);
        Class<T> clas = getPoClass();
        String tableName = getTableName();
        Map<String, String> javaDbFieldMapping = getJavaDbFieldMapping(clas);
        List<String> dbFieldNames = new ArrayList<>();
        List<String> javaFieldNames = new ArrayList<>();
        List<String> qlist = new ArrayList<>();

        javaDbFieldMapping.forEach((k,v) -> {
            if (!StringUtils.equals(k, "id")) {
                javaFieldNames.add(k);
                dbFieldNames.add(v);
                qlist.add("?");
                params.add(bw.getPropertyValue(k));
            }
        });

        String format = "insert into %s(%s) values(%s) ";
        return String.format(format, tableName, StringUtils.join(dbFieldNames, ", "), StringUtils.join(qlist, ", "));
    }

    private String buildSql_update(T t, List<Object> params) {
        BeanWrapper bw = new BeanWrapperImpl(t);
        Class<T> clas = getPoClass();
        String tableName = getTableName();
        Map<String, String> javaDbFieldMapping = getJavaDbFieldMapping(clas);

        String format = "update %s set %s where id=? ";
        List<String> updateFieldNames = new ArrayList<>();
        javaDbFieldMapping.forEach((k,v) -> {
            if (!StringUtils.equals(k, "id")) {
                updateFieldNames.add(v+"=?");
                params.add(bw.getPropertyValue(k));
            }
        });

        params.add(t.getId());
        return String.format(format, tableName, StringUtils.join(updateFieldNames, ", "));
    }

    private String buildSql_delete(T t, List<Object> params) {
        String tableName = getTableName();
        String format = "delete from %s where id=? ";
        params.add(t.getId());
        return String.format(format, tableName);
    }

    private String buildSql_deleteByIds(List<Long> ids) {
        String tableName = getTableName();
        String format = "delete from %s where id in (%s) ";
        return String.format(format, tableName, StringUtils.join(ids, ","));
    }

    private String getDbIdFieldName() {
        Class<T> clas = getPoClass();
        Map<String, String> mapping = getJavaDbFieldMapping(clas);
        return mapping.get("id");
    }

    private String getTableName() {
        Class<T> clas = getPoClass();
        Annotation[] annoArr = clas.getDeclaredAnnotationsByType(Table.class);
        if (annoArr != null && annoArr.length > 0) {
            return ((Table)annoArr[0]).name();
        } else {
            throw new RuntimeException("Table name can not be found. ");
        }
    }

    private Map<String, String> getJavaDbFieldMapping(Class clas) {
        Field[] fields = clas.getDeclaredFields();

        Map<String, String> javaDbFieldMapping = new HashMap<String, String>();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                String dbFieldName = getDbFieldName(field);
                if (dbFieldName != null) {
                    javaDbFieldMapping.put(field.getName(), dbFieldName);
                }
            }
        }

        Class superClas = clas.getSuperclass();
        if (BasePo.class.isAssignableFrom(superClas)) {
            javaDbFieldMapping.putAll(getJavaDbFieldMapping(superClas));
        }

        return javaDbFieldMapping;
    }

    private String getDbFieldName(Field field) {
        Annotation[] annoArr = field.getDeclaredAnnotationsByType(Column.class);
        if (annoArr != null && annoArr.length > 0) {
            return ((Column) annoArr[0]).name().toUpperCase();
        } else {
            return null;
        }
    }

    public void deleteByIds(List<Long> ids) {
        Assert.notEmpty(ids, "参数不能为空。");
        String sql = buildSql_deleteByIds(ids);
        jt.update(sql);
    }

    protected <E> Page<E> findPageBySql(String sql, List params, int pageIndex, int pageSize,
                                             Class<E> clazz) {
        String sqlCount = "select count(1) from (" + sql + ") t_main ";
        int offset = (pageIndex-1)*pageSize;
        String sqlList = "select * from (" + sql + ") t_main limit " + offset + ", " + pageSize;
        Page<E> page = new Page<>();
        page.setCount(jt.queryForObject(sqlCount, params.toArray(), Long.class));
        page.setDataList(findBySql(sqlList, params, clazz));
        return page;
    }

    public <E> List<E> findBySql(String sql, List<Object> params, Class<E> clazz) {
        List<Map<String, Object>> mlist = jt.queryForList(sql, params.toArray());
        List<E> rlist = new ArrayList<>();
        if (CollectionUtils.isEmpty(mlist)) {
            return rlist;
        } else {
            mlist.forEach(m->{rlist.add(map2Po(m, clazz));});
            return rlist;
        }
    }

    private <E> E map2Po(Map<String, Object> m, Class<E> clazz) {
        if (m == null) {
            return null;
        }

        E e = null;
        try {
            e = clazz.newInstance();
        } catch (Exception e1) {
            throw new RuntimeException(e1.getMessage());
        }

        BeanWrapper bw = new BeanWrapperImpl(e);
        Map<String, String> javaDbFieldMapping = getJavaDbFieldMapping(clazz);
        Map<String, String> dbJavaFieldMapping = new HashMap<String, String>();
        javaDbFieldMapping.forEach((k, v) -> {
            dbJavaFieldMapping.put(v, k);
        });

        m.forEach((k, v) -> {
            String dbFieldName = k.toUpperCase();
            String javaFieldName = dbJavaFieldMapping.get(dbFieldName);
            bw.setPropertyValue(javaFieldName, v);
        });

        return e;
    }
}
