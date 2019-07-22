package com.framework.middleware.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/07/20 17:03
 * @description：注解类 对Mapper类上的方法做业务描述
 * @modified By：
 * @version: 1$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RefBusinessOperation {
    String value();
}
