package com.framework.middleware.plugin;

import com.framework.middleware.abstractUtil.CollectionUtil;
import com.framework.middleware.abstractUtil.StringUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hibernate.validator.internal.util.ReflectionHelper.isList;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/16 10:44
 * @description：SQL执行插件，打印SQL执行语句与时间
 * @modified By：
 * @version: $1
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = { Statement.class })
})
public class SqlExectorPlugin implements Interceptor {
    private static Logger logger = Logger.getLogger(String.valueOf(SqlExectorPlugin.class));
    private static final String regx = "\\?";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        StatementHandler statementHandler = (StatementHandler)target;

        long startTime = System.currentTimeMillis();
        Object proceed = invocation.proceed();
        long endTime = System.currentTimeMillis();
        long sqlCost = endTime - startTime;

        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = "执行耗时:["+sqlCost+"ms]"+"  SQL:"+boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

        sql = formatSql(sql, parameterObject, parameterMappingList);
        logger.debug(sql);
        return proceed;
    }

    /**
     * 格式化参数
     * @param sql
     * @param parameterObject
     * @param parameterMappingList
     * @return
     */
    private String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        //1.判断空
        if(StringUtil.isEmptyOrNull(sql)){
            return "";
        }

        //2.如果是不传入任何参数的话，直接返回
        if(parameterObject==null|| CollectionUtil.isEmpty(parameterMappingList)){
            return sql;
        }

        Class<?> parameterObjectClass = parameterObject.getClass();
        // 如果参数是StrictMap且Value类型为Collection，获取key="list"的属性，这里主要是为了处理<foreach>循环时传入List这种参数的占位符替换
        // 例如select * from xxx where id in <foreach collection="list">...</foreach>
        if(parameterObjectClass.isAssignableFrom(StrictMap.class)){
            StrictMap<Collection<?>> strictMap = (StrictMap<Collection<?>>)parameterObject;
                if (isList(strictMap.get("list").getClass())) {
                         sql = handleListParameter(sql, strictMap.get("list"));
                }
        }else if(isMap(parameterObjectClass)){
            // 如果参数是Map则直接强转，通过map.get(key)方法获取真正的属性值
            // 这里主要是为了处理<insert>、<delete>、<update>、<select>时传入parameterType为map的场景
            Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
            sql = handleMapParameter(sql, paramMap, parameterMappingList);
        }else{
            // 通用场景，比如传的是一个自定义的对象或者八种基本数据类型之一或者String
            try {
                sql = handleCommonParameter(sql, parameterMappingList, parameterObjectClass, parameterObject);
            } catch (NoSuchFieldException|IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sql;
    }

    private String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList, Class<?> parameterObjectClass, Object parameterObject) throws NoSuchFieldException, IllegalAccessException {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            String propertyValue = null;
            // 基本数据类型或者基本数据类型的包装类，直接toString即可获取其真正的参数值，其余直接取paramterMapping中的property属性即可
            if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
                propertyValue = String.valueOf(parameterObject);
            }else{
                String propertyName = parameterMapping.getProperty();
                Field field = parameterObjectClass.getDeclaredField(propertyName);
                field.setAccessible(true);
                propertyValue = String.valueOf(field.get(parameterObject));
                if (parameterMapping.getJavaType().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
            }
            sql = sql.replaceFirst(regx, propertyValue);
        }
        return sql;
    }

    private String handleMapParameter(String sql, Map<?,?> paramMap, List<ParameterMapping> parameterMappingList) {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            Object propertyName = parameterMapping.getProperty();
            Object propertyValue = paramMap.get(propertyName);
            if (propertyValue != null) {
                if (propertyValue.getClass().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
                sql = sql.replaceFirst(regx, propertyValue.toString());
            }
        }
        return  sql;
    }

    private boolean isMap(Class<?> parameterObjectClass) {
        Class<?>[] interfaceClasses = parameterObjectClass.getInterfaces();
        for(Class<?> interfaceClass : interfaceClasses){
            if(interfaceClass.isAssignableFrom(Map.class)){
                return true;
            }
        }
        return false;
    }

    private String handleListParameter(String sql, Collection<?> list) {
        if(CollectionUtil.isNotEmpty(list)){
            for (Object o:list) {
                String value = null;
                Class<?> clazz = o.getClass();
                if (isPrimitiveOrPrimitiveWrapper(clazz)) {
                    value = o.toString();
                }else if (clazz.isAssignableFrom(String.class)){
                    value = "\"" + o.toString() + "\"";
                }
                sql = sql.replaceFirst(regx, value);
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private  boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
        return parameterObjectClass.isPrimitive()||
                (parameterObjectClass.isAssignableFrom(Byte.class) || parameterObjectClass.isAssignableFrom(Short.class) ||
                        parameterObjectClass.isAssignableFrom(Integer.class) || parameterObjectClass.isAssignableFrom(Long.class) ||
                        parameterObjectClass.isAssignableFrom(Double.class) || parameterObjectClass.isAssignableFrom(Float.class) ||
                        parameterObjectClass.isAssignableFrom(Character.class) || parameterObjectClass.isAssignableFrom(Boolean.class));
    }
}
