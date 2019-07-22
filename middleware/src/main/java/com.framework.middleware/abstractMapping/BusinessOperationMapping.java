package com.framework.middleware.abstractMapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/20 17:13
 * @description：业务操作映射表
 * @modified By：
 * @version: 1$
 */
public class BusinessOperationMapping {
    /**私有化构造 不允许new**/
    private BusinessOperationMapping(){}

    public static String matchOperation(String meta){
       return BusinessMetaUtil.match(meta,BusinessOperationMapping.class);
    }

    public static final String CREATE = "创建";
    public static final String INSERT = "插入";
    public static final String MODIFY = "修改";
    public static final String DELETE = "删除";
}
