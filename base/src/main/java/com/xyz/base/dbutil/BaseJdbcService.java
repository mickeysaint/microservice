package com.xyz.base.dbutil;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseJdbcService<T extends BaseEntity> {

    public static String sql_locale = "mysql";

    protected JdbcTemplate jt;

    protected Class<T> entityClass;

    public BaseJdbcService(JdbcTemplate jt) {
            entityClass = getEntityClass();
            this.jt = jt;
    }

    private Class<T> getEntityClass() {
        Type t = this.getClass().getGenericSuperclass();
        Type[] tarr = ((ParameterizedType)t).getActualTypeArguments();
        return (Class<T>)tarr[0];
    }

    public T findById(long id) {
        String sql = buildSql_findById(entityClass);
        return map2Po(jt.queryForMap(sql, new Object[]{id}), entityClass);
    }

    public void save(T t) {
        List<Object> params = new ArrayList<>();
        String sql = buildSql_save(t, params);
        jt.update(sql, params.toArray());
        Long id = jt.queryForObject("select last_insert_id()", Long.class);
        t.setId(id);
    }

    public void update(T t) {
        List<Object> params = new ArrayList<>();
        String sql = buildSql_update(t, params);
        jt.update(sql, params.toArray());
    }

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
            mlist.forEach(m->{rlist.add(map2Po(m, entityClass));});
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

    public Page<T> findPageBySql(String sqlCount, String sqlList, List<Object> params, int pageIndex, int pageSize) {
        int offset = (pageIndex-1)*pageSize;
        sqlList += sqlList + " limit " + offset + ", " + pageSize;
        Page<T> page = new Page<>();
        page.setCount(jt.queryForObject(sqlCount, params.toArray(), Long.class));
        page.setDataList(findBySql(sqlList, params));
        return page;
    }

    private String buildSql_findById(Class<T> clas) {
        String clasName = clas.getSimpleName();
        String tableName = convertToDbStyle(clasName);
        List<String> fieldNames = getFieldNames(clas);
        List<String> sqlFields = new ArrayList<>();
        fieldNames.forEach(s -> {sqlFields.add(convertToDbStyle(s));});
        String format = "select %s from %s where id=?";
        return String.format(format, StringUtils.join(sqlFields, ", "), tableName);
    }

    private String buildSql_findByEg(T eg, List<Object> params) {
        String clasName = entityClass.getSimpleName();
        String tableName = convertToDbStyle(clasName);
        List<String> fieldNames = getFieldNames(entityClass);
        List<String> sqlFields = new ArrayList<>();
        fieldNames.forEach(s -> {sqlFields.add(convertToDbStyle(s));});
        String format = "select %s from %s where 1=1 %s ";

        BeanWrapper bw = new BeanWrapperImpl(eg);
        List<String> conditionList = new ArrayList<>();
        for (String fn : fieldNames) {
            Object v = bw.getPropertyValue(fn);
            if (v != null) {
                params.add(v);
                conditionList.add(convertToDbStyle(fn) + "=?");
            }
        }

        if (conditionList.size() > 0) {
            return String.format(format, StringUtils.join(sqlFields, ", "), tableName,
                    "and " + StringUtils.join(conditionList, " and "));
        } else {
            return String.format(format, StringUtils.join(sqlFields, ", "), tableName,
                    "");
        }
    }

    private String buildSql_save(T t, List<Object> params) {
        BeanWrapper bw = new BeanWrapperImpl(t);
        String clasName = entityClass.getSimpleName();
        String tableName = convertToDbStyle(clasName);
        List<String> fieldNames = getFieldNames(entityClass);
        String format = "insert into %s(%s) values(%s) ";
        List<String> dbFieldNames = new ArrayList<>();
        List<String> qlist = new ArrayList<>();
        fieldNames.forEach(s -> {
            if (!StringUtils.equals(s, "id")) {
                dbFieldNames.add(convertToDbStyle(s));
                qlist.add("?");
                params.add(bw.getPropertyValue(s));
            }
        });
        return String.format(format, tableName, StringUtils.join(dbFieldNames, ", "), StringUtils.join(qlist, ", "));
    }

    private String buildSql_update(T t, List<Object> params) {
        BeanWrapper bw = new BeanWrapperImpl(t);
        String clasName = entityClass.getSimpleName();
        String tableName = convertToDbStyle(clasName);
        List<String> fieldNames = getFieldNames(entityClass);
        String format = "update %s set %s where id=? ";
        List<String> dbFieldNames = new ArrayList<>();
        List<String> qlist = new ArrayList<>();
        fieldNames.forEach(s -> {
            if (!StringUtils.equals(s, "id")) {
                dbFieldNames.add(convertToDbStyle(s) + "=?");
                params.add(bw.getPropertyValue(s));
            }
        });

        params.add(t.getId());
        return String.format(format, tableName, StringUtils.join(dbFieldNames, ", "));
    }

    private String buildSql_delete(T t, List<Object> params) {
        String clasName = entityClass.getSimpleName();
        String tableName = convertToDbStyle(clasName);
        String format = "delete from %s where id=? ";
        params.add(t.getId());
        return String.format(format, tableName);
    }

    private String convertToDbStyle(String s) {
        StringBuffer sbuf = new StringBuffer();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c >='A' && c<='Z') {
                if (i == 0) {
                    sbuf.append((c+"").toLowerCase());
                } else {
                    sbuf.append(("_" + c).toLowerCase());
                }
            } else {
                sbuf.append(c);
            }
        }
        return sbuf.toString();
    }

    private String convertToJavaStyle(String s) {
        s = s.toLowerCase();
        StringBuffer sbuf = new StringBuffer();
        int i=0;
        while(i<s.length()) {
            char c = s.charAt(i);
            if (c == '_') {
                if (i+1<s.length()) {
                    sbuf.append((s.charAt(i+1)+"").toUpperCase());
                    i = i+2;
                }
            } else {
                sbuf.append(c);
                i++;
            }
        }
        return sbuf.toString();
    }

    private List<String> getFieldNames(Class<T> clas) {
        Field[] fields = clas.getDeclaredFields();

        List<String> fieldNameList = new ArrayList<>();
        for (int i=0; i<fields.length; i++) {
            Field field = fields[i];
            Annotation[] annoArr = field.getDeclaredAnnotations();
            if (hasIgnore(annoArr)) {
                continue;
            }
            fieldNameList.add(field.getName());
        }

        Class superClas = clas.getSuperclass();
        if (BaseEntity.class.isAssignableFrom(superClas)) {
            fieldNameList.addAll(getFieldNames(superClas));
        }
        return fieldNameList;
    }

    private boolean hasIgnore(Annotation[] annoArr) {
        boolean ret = false;
        if (annoArr != null && annoArr.length > 0) {
            for (Annotation anno : annoArr) {
                if (anno instanceof Ignore) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    private T map2Po(Map<String, Object> m, Class<T> c) {
        if (m == null) {
            return null;
        }

        T t = null;
        try {
            t = c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        BeanWrapper bw = new BeanWrapperImpl(t);
        for(Map.Entry<String, Object> e : m.entrySet()) {
            bw.setPropertyValue(convertToJavaStyle(e.getKey()), e.getValue());
        }
        return t;
    }

}
