package com.framework.middleware.abstractMapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/20 17:12
 * @description：业务对象映射表
 * @modified By：
 * @version: 1$
 */
public class BusinessMetaMapping {
    /**私有化构造 不允许new**/
    private BusinessMetaMapping(){}

    public static String matchBusinessMeta(String meta){
        return BusinessMetaUtil.match(meta,BusinessMetaMapping.class);
    }

    /**基础档案映射关系表**/
    public static final String USER = "用户档案";
    public static final String STAFF = "员工档案";
    public static final String CHANNEL = "渠道档案";


    /**业务档案映射关系表**/
}
