package com.framework.middleware.abstractMapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/20 21:07
 * @description：业务关系表Util工具类
 * @modified By：
 * @version: 1$
 */
public class BusinessMetaUtil {

    BusinessMetaUtil(){}

    /**
     * 匹配对应的业务对象或者操作
     * @param metaOrOperation
     * @return
     */
    public static String match(String metaOrOperation,Class clazz){
        Field[] declaredFields = clazz.getDeclaredFields();
        Optional<Field> optionalField = Arrays.asList(declaredFields).stream().filter(n -> n.getName().equalsIgnoreCase(metaOrOperation)||metaOrOperation.toUpperCase().contains(n.getName())).findFirst();
        if(optionalField.isPresent()){
            try {
                return String.valueOf(optionalField.get().get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return metaOrOperation;
    }
}
